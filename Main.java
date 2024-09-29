import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        call_task1();
        call_task2();
        call_task3();
    }
    private static void call_task1() throws InterruptedException, ExecutionException {
        int[] arr = new int[10000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 100);
        }
        // first way
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.currentTimeMillis();
        System.out.println("Сумма элементов: " + Task1.firstWay(arr));
        long endTime = System.currentTimeMillis();
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Последовательно: " + (endTime - startTime) + " ms");
        System.out.println("Последовательно: использованная память: " + (memoryAfter - memoryBefore) + " байт");

        // second way
        runtime = Runtime.getRuntime();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.currentTimeMillis();
        System.out.println("\nСумма элементов: " + Task1.secondWay(arr, 5));
        endTime = System.currentTimeMillis();
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("С использованием Thread: " + (endTime - startTime) + " ms");
        System.out.println("С использованием Thread: использованная память: " + (memoryAfter - memoryBefore) + " байт");

        // third way
        runtime = Runtime.getRuntime();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool();  // Создаем пул ForkJoin
        ForkJoinSum task = new ForkJoinSum(arr, 0, arr.length);
        pool.invoke(task);
        System.out.println("\nСумма элементов: " + pool.invoke(task));
        endTime = System.currentTimeMillis();
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Fork join: " + (endTime - startTime) + " ms");
        System.out.println("Fork join: использованная память: " + (memoryAfter - memoryBefore) + " байт\n");
    }
    private static void call_task2() {
        Task2.task2();
    }
    private static void call_task3() {
        BlockingQueue<Task3> queue = new LinkedBlockingQueue<>(5); // Очередь вместимостью 5 файлов
        // Создаем генератор файлов и обработчики для разных типов файлов
        Thread generatorThread = new Thread(new FileGenerator(queue));
        Thread jsonProcessorThread = new Thread(new FileProcessor(queue,
                "JSON"));
        Thread xmlProcessorThread = new Thread(new FileProcessor(queue, "XML"));
        Thread xlsProcessorThread = new Thread(new FileProcessor(queue, "XLS"));
        // Запускаем потоки
        generatorThread.start();
        jsonProcessorThread.start();
        xmlProcessorThread.start();
        xlsProcessorThread.start();
    }
}
