package mirea.egor.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import mirea.egor.Entities.Mamont;
import mirea.egor.Repositories.MamontRepository;
import mirea.egor.Exceptions.CustomException;
@RestController
@RequestMapping("/mamonts")
public class MamontController {
    private final MamontRepository mamontRepository;
    @Autowired
    public MamontController(MamontRepository mamontRepository) {
        this.mamontRepository = mamontRepository;
    }
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mamont>> getMamontById(@PathVariable("id") Long id) {
        return mamontRepository.findById(id)
                .map(mamont -> ResponseEntity.ok(mamont))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping
    public Flux<Mamont> getAllMamonts(@RequestParam(name = "minage", required = false)
                                Integer minAge) {
        Flux<Mamont> mamonts = mamontRepository.findAll();
        if (minAge != null && minAge > 0) {
            // Если параметр "minage" указан, применяем фильтрацию
            mamonts = mamonts.filter(mamont -> mamont.getAge() >= minAge);
        }
        return mamonts
                .map(this::transformMamont) // Применение оператора map для преобразования объектов Mamont
                .onErrorResume(e -> {
                    // Обработка ошибок
                    return Flux.error(new CustomException("Failed to fetch mamonts", e));
                })
                .onBackpressureBuffer(); // Работа в формате backpressure
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Mamont> createMamont(@RequestBody Mamont mamont) {
        return mamontRepository.save(mamont);
    }
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Mamont>> updateMamont(@PathVariable("id") Long id,
                                               @RequestBody Mamont updatedMamont) {
        return mamontRepository.findById(id)
                .flatMap(existingMamont -> {
                    existingMamont.setName(updatedMamont.getName());
                    existingMamont.setColor(updatedMamont.getColor());
                    existingMamont.setAge(updatedMamont.getAge());
                    return mamontRepository.save(existingMamont);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMamont(@PathVariable("id") Long id) {
        return mamontRepository.findById(id)
                .flatMap(existingMamont ->
                        mamontRepository.delete(existingMamont)
                                .then(Mono.just(new
                                        ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    private Mamont transformMamont(Mamont mamont) {
        // Пример преобразования объекта Mamont
        mamont.setName(mamont.getName().toUpperCase()); // Преобразование имени мамонта в верхний регистр
        return mamont;
    }
}
