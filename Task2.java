import java.util.Scanner;
import java.util.concurrent.*;

public class Task2 {
    public static int processTask(int number) throws InterruptedException {
        int delay = ThreadLocalRandom.current().nextInt(1, 6);
        Thread.sleep(delay * 1000L);
        return number * number;
    }
    public static void task2() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите число (или 'exit' для выхода):");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                int number = Integer.parseInt(input);
                Future<Integer> future = executorService.submit(() -> processTask(number));
                System.out.println("Обрабатываем запрос...");
                System.out.println("Результат: " + future.get());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Введите корректное число.");
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Произошла ошибка при обработке задачи.");
            }
        }
        // Завершаем работу пула потоков
        executorService.shutdown();
    }
}
