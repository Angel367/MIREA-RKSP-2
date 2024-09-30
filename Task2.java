import io.reactivex.Observable;
import java.util.Random;

public class Task2 {
    // Задача 2.1.1: Преобразовать поток случайных чисел в поток квадратов
    public void task2_1_1() {
        System.out.println("Исходный поток случайных чисел:");
        Observable<Integer> originalStream = Observable
                .range(0, 1000)
                .map(i -> {
                    Random random = new Random();
                    return random.nextInt(1001); // Случайное число от 0 до 1000
                })
                .cache(); // Кэшируем поток для повторного использования
        // Выводим первые 5 элементов исходного потока
        originalStream.take(5).subscribe(System.out::println);
        System.out.println("\nПреобразованный поток (квадраты чисел):");
        // Преобразованный поток (квадраты чисел)
        Observable<Integer> squaredStream = originalStream
                .map(number -> number * number); // Применяем оператор map для вычисления квадратов чисел
        // Выводим первые 5 элементов преобразованного потока
        squaredStream.take(5).subscribe(System.out::println);
    }
    // Задача 2.2.1: Объединить два потока — букв и цифр
    public void task2_2_1() {
        // Генерируем поток случайных букв
        Observable<String> letters = Observable
                .range(0, 1000)
                .map(i -> {
                    Random random = new Random();
                    return String.valueOf((char) (random.nextInt(26) + 'A')); // Случайная буква от A до Z
                })
                .cache(); // Кэшируем поток для повторного использования
        // Генерируем поток случайных цифр
        Observable<Integer> numbers = Observable
                .range(0, 1000)
                .map(i -> {
                    Random random = new Random();
                    return random.nextInt(10); // Случайное число от 0 до 9
                })
                .cache(); // Кэшируем поток для повторного использования

        System.out.println("Исходный поток букв:");
        letters.take(5).subscribe(System.out::println); // Выводим первые 5 букв

        System.out.println("\nИсходный поток цифр:");
        numbers.take(5).subscribe(System.out::println); // Выводим первые 5 цифр

        System.out.println("\nОбъединенный поток (буква + цифра):");
        // Объединяем буквы и цифры
        Observable<String> mergedStream = Observable.zip(
                letters, numbers, (letter, number) -> letter + number);

        // Выводим первые 5 элементов объединенного потока
        mergedStream.take(5).subscribe(System.out::println);
    }
    // Задача 2.3.1: Пропустить первые три элемента из потока случайных чисел
    public void task2_3_1() {
        System.out.println("Исходный поток случайных чисел:");
        // Генерируем поток случайных чисел
        Observable<Integer> originalStream = Observable
                .range(0, 10)
                .map(i -> {
                    Random random = new Random();
                    return random.nextInt(100); // Случайное число от 0 до 100
                })
                .cache(); // Кэшируем поток для повторного использования
        // Выводим все элементы исходного потока
        originalStream.take(10).subscribe(System.out::println);
        System.out.println("\nПоток без первых трех элементов:");
        // Пропускаем первые три элемента
        Observable<Integer> resultStream = originalStream.skip(3);
        // Выводим оставшиеся 7 элементов
        resultStream.take(7).subscribe(System.out::println);
    }
    public static void main(String[] args) {
        Task2 tasks = new Task2();

        System.out.println("Задача 2.1.1: Преобразование в квадраты чисел");
        tasks.task2_1_1();

        System.out.println("\nЗадача 2.2.1: Объединение букв и цифр");
        tasks.task2_2_1();

        System.out.println("\nЗадача 2.3.1: Пропуск первых трех элементов");
        tasks.task2_3_1();
    }
}
