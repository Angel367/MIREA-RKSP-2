import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        call_task1();
        call_task2();
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
        System.out.println("First way: " + (endTime - startTime) + " ms");
        System.out.println("Использованная память: " + (memoryAfter - memoryBefore) + " байт");

        // second way
        runtime = Runtime.getRuntime();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.currentTimeMillis();
        System.out.println("Сумма элементов: " + Task1.secondWay(arr, 5));
        endTime = System.currentTimeMillis();
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Second way: " + (endTime - startTime) + " ms");
        System.out.println("Использованная память: " + (memoryAfter - memoryBefore) + " байт");

        // third way
        runtime = Runtime.getRuntime();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool();  // Создаем пул ForkJoin
        ForkJoinSum task = new ForkJoinSum(arr, 0, arr.length);
        pool.invoke(task);
        System.out.println("Сумма элементов: " + pool.invoke(task));
        endTime = System.currentTimeMillis();
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Third way: " + (endTime - startTime) + " ms");
        System.out.println("Использованная память: " + (memoryAfter - memoryBefore) + " байт");
    }
    private static void call_task2() {
        Task2.task2();
    }
}
