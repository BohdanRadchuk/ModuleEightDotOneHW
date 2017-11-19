import operations.ArrayOperations;

import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class TaskTwoMain {
    static final int ARRAY_SIZE = 80000;
    static AtomicReference<Double> summ;
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphorePoolvsThread = new Semaphore(1);
        ArrayOperations arrayOps = new ArrayOperations();
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many repeats do you want to do?");
        int repeats = scanner.nextInt();
        arrayOps.intArrayCreation(ARRAY_SIZE);
        final int processors = Runtime.getRuntime().availableProcessors();


        semaphorePoolvsThread.acquire();
        ExecutorService threadPool = Executors.newFixedThreadPool(processors);
        for (int i = 0; i<processors; i++) {


            int finalI = i;
            Callable<Double> callable = () -> arrayOps.calculateResult(finalI * (ARRAY_SIZE / processors), (finalI + 1) * ARRAY_SIZE / processors);
            FutureTask<Double> futureTask = new FutureTask<Double>(callable);

            Future<Double> future = threadPool.submit(callable);
            future = threadPool.submit(callable);

        }

        for (int i = 0; i<processors; i++){
            final int count = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    summ.set(summ.get() + arrayOps.calculateResult(count*(ARRAY_SIZE/processors),(count+1)*ARRAY_SIZE/processors));
                }
            };
        Thread thread = new Thread(runnable);
        thread.start();}

    }
}
