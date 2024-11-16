package mirea.egor;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import mirea.egor.Entities.Mamont;
import mirea.egor.Repositories.MamontRepository;
import mirea.egor.Controllers.MamontController;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MamontControllerTest {
    @Test
    public void testGetMamontById() {
        // Создайте фиктивного мамонта
        Mamont mamont = new Mamont();
        mamont.setId(1L);
        mamont.setName("Whiskers");
        // Создайте мок репозитория
        MamontRepository mamontRepository = Mockito.mock(MamontRepository.class);
        when(mamontRepository.findById(1L)).thenReturn(Mono.just(mamont));
        // Создайте экземпляр контроллера
        MamontController mamontController = new MamontController(mamontRepository);
        // Вызовите метод контроллера и проверьте результат
        ResponseEntity<Mamont> response = mamontController.getMamontById(1L).block();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mamont, response.getBody());
    }
    @Test
    public void testGetAllMamonts() {
        // Создайте список фиктивных мамонтов
        Mamont mamont1 = new Mamont();
        mamont1.setId(1L);
        mamont1.setName("Whiskers");
        mamont1.setAge(3);
        Mamont mamont2 = new Mamont();
        mamont2.setId(2L);
        mamont2.setName("Fluffy");
        mamont2.setAge(4);
        // Создайте мок репозитория
        MamontRepository mamontRepository = Mockito.mock(MamontRepository.class);
        when(mamontRepository.findAll()).thenReturn(Flux.just(mamont1, mamont2));
        // Создайте экземпляр контроллера
        MamontController mamontController = new MamontController(mamontRepository);
        // Вызовите метод контроллера и проверьте результат
        Flux<Mamont> response = mamontController.getAllMamonts(null);
        assertEquals(2, response.collectList().block().size());
    }
    @Test
    public void testCreateMamont() {
        // Создайте фиктивного мамонта
        Mamont mamont = new Mamont();
        mamont.setId(1L);
        mamont.setName("Whiskers");
        // Создайте мок репозитория
        MamontRepository mamontRepository = Mockito.mock(MamontRepository.class);
        when(mamontRepository.save(mamont)).thenReturn(Mono.just(mamont));
        // Создайте экземпляр контроллера
        MamontController mamontController = new MamontController(mamontRepository);
        // Вызовите метод контроллера и проверьте результат
        Mono<Mamont> response = mamontController.createMamont(mamont);
        assertEquals(mamont, response.block());
    }
    @Test
    public void testUpdateMamont() {
        // Создайте фиктивного мамонта
        Mamont existingMamont = new Mamont();
        existingMamont.setId(1L);
        existingMamont.setName("Whiskers");
        // Создайте фиктивного обновленного мамонта
        Mamont updatedMamont = new Mamont();
        updatedMamont.setId(1L);
        updatedMamont.setName("Fluffy");
        // Создайте мок репозитория
        MamontRepository mamontRepository = Mockito.mock(MamontRepository.class);
        when(mamontRepository.findById(1L)).thenReturn(Mono.just(existingMamont));
        when(mamontRepository.save(existingMamont)).thenReturn(Mono.just(updatedMamont));
        // Создайте экземпляр контроллера
        MamontController mamontController = new MamontController(mamontRepository);
        // Вызовите метод контроллера и проверьте результат
        ResponseEntity<Mamont> response = mamontController.updateMamont(1L,
                updatedMamont).block();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedMamont, response.getBody());
    }
    @Test
    public void testDeleteMamont() {
        // Создайте фиктивного мамонта
        Mamont mamont = new Mamont();
        mamont.setId(1L);
        mamont.setName("Whiskers");
        // Создайте мок репозитория
        MamontRepository mamontRepository = Mockito.mock(MamontRepository.class);
        when(mamontRepository.findById(1L)).thenReturn(Mono.just(mamont));
        when(mamontRepository.delete(mamont)).thenReturn(Mono.empty());
        // Создайте экземпляр контроллера
        MamontController mamontController = new MamontController(mamontRepository);
        // Вызовите метод контроллера и проверьте результат
        ResponseEntity<Void> response = mamontController.deleteMamont(1L).block();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}