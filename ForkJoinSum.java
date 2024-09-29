import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;
public class ForkJoinSum extends RecursiveTask<Integer> {
    private final int[] array;
    private final int start;
    private final int end;
    private static final int THRESHOLD = 3;
    // Конструктор для задания диапазона массива
    public ForkJoinSum(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }
    @Override
    protected Integer compute() {
        if (end - start <= THRESHOLD) {
            int sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
                try {
                    Thread.sleep(1); // sleep for 1 ms
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return sum;
        } else {
            int mid = (start + end) / 2;
            ForkJoinSum leftTask = new ForkJoinSum(array, start, mid);
            ForkJoinSum rightTask = new ForkJoinSum(array, mid, end);
            leftTask.fork();
            int rightResult = rightTask.compute();
            int leftResult = leftTask.join();
            return leftResult + rightResult;
        }
    }
}