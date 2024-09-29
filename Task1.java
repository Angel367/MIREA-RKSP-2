import java.util.concurrent.*;
public class Task1 {
    public static int firstWay(int[] arr) throws InterruptedException {
        int sum = 0;
        for (int j : arr) {
            sum += j;
            Thread.sleep(1);     // sleep for 1 ms
        }
        return sum;
    }
    public static int secondWay(int[] arr, int numThreads) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads); // создаем пул потоков
        int len = arr.length;  // длина массива
        int chunkSize = len / numThreads;  // размер части для каждого потока
        Future<Integer>[] futures = new Future[numThreads]; // массив Future для хранения результатов
        // Запускаем каждый поток для расчета суммы своей части массива
        for (int i = 0; i < numThreads; i++) {
            final int start = i * chunkSize;
            final int end = (i == numThreads - 1) ? len : (i + 1) * chunkSize;
            // Определяем задачу для потока
            futures[i] = executor.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws InterruptedException {
                    int sum = 0;
                    for (int j = start; j < end; j++) {
                        sum += arr[j];
                        Thread.sleep(1); // sleep for 1 ms
                    }
                    return sum;
                }
            });
        }
        int totalSum = 0;
        // Собираем результаты из всех потоков
        for (int i = 0; i < numThreads; i++) {
            totalSum += futures[i].get(); // get() блокирует выполнение до завершения потока
        }
        executor.shutdown(); // Останавливаем ExecutorService
        return totalSum;
    }
}
