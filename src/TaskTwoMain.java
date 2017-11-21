import operations.ArrayOperations;

import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class TaskTwoMain {
    static final int ARRAY_SIZE = 80000;
    static double sumDouble = 0;
    static AtomicReference<Double> summ = new AtomicReference<>(0.0);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Semaphore semaphorePoolvsThread = new Semaphore(1);
        ArrayOperations arrayOps = new ArrayOperations();
        Scanner scanner = new Scanner(System.in);
        //System.out.println("How many repeats do you want to do?");
        //int repeats = scanner.nextInt();
        int repeats = 1;
                arrayOps.intArrayCreation(ARRAY_SIZE);
        final int processors = Runtime.getRuntime().availableProcessors();
        System.out.println("amount of processors = " + processors);


        long timer = System.currentTimeMillis();
        System.out.println("Time at start of pool work = " + timer);
        ExecutorService threadPool = Executors.newFixedThreadPool(processors);
        while (repeats > 0) {
            semaphorePoolvsThread.acquire();
            summ.set(0.0);
            for (int i = 0; i < processors; i++) {
                int finalI = i;
                Callable<Double> doubleCallable = () -> arrayOps.calculateResult(finalI * (ARRAY_SIZE / processors), (finalI + 1) * ARRAY_SIZE / processors);
                FutureTask<Double> futureTask = new FutureTask<>(doubleCallable);

                threadPool.submit(futureTask);
                sumDouble += futureTask.get();
                summ.set(summ.get() + futureTask.get());

            }
            System.out.println("Total AtomicSumm = " + summ + " from thread pool");


            semaphorePoolvsThread.release();
            repeats--;
        }
        threadPool.shutdown();
        timer -= System.currentTimeMillis();
        System.out.println("pool was working for " + (-1) * timer + "ms");


        semaphorePoolvsThread.acquire();

        summ.set(0.0);
        sumDouble = 0;

        for (int i = 0; i < processors; i++) {
            final int count = i;
            double tempFuture = 0;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    // Thread.currentThread().wait(1000);
                    System.out.println(arrayOps.calculateResult(count * (ARRAY_SIZE / processors), (count + 1) * ARRAY_SIZE / processors));
                    arrayOps.calculateResult(count * (ARRAY_SIZE / processors), (count + 1) * ARRAY_SIZE / processors);
                    /*summ.set(summ.get() + arrayOps.calculateResult(count*(ARRAY_SIZE/processors),(count+1)*ARRAY_SIZE/processors));
                    System.out.println(sumDouble);*/
                    //sumDouble+=arrayOps.calculateResult(0,80000);
                    /*
                    System.out.println(summ.get() + "atomic");
                */
                }
            };

            FutureTask<Double> futureTask = new FutureTask<Double>(runnable, tempFuture);
            new Thread(futureTask).start();
            System.out.println(futureTask.get() + "future get");
            sumDouble += futureTask.get();
            System.out.println(sumDouble + "summdouble at end");

        }

        semaphorePoolvsThread.release();
        System.out.println("summdouble" + sumDouble );
        System.out.println(summ.get());
    }
}
