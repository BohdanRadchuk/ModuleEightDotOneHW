import operations.ArrayOperations;

import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class TaskTwoMain {
    static final int ARRAY_SIZE = 80000;
    static AtomicReference<Double> summ = new AtomicReference<>(0.0);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Semaphore semaphorePoolvsThread = new Semaphore(1);
        ArrayOperations arrayOps = new ArrayOperations();
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many repeats do you want to do?");
        int totalRepeats = scanner.nextInt();
        arrayOps.intArrayCreation(ARRAY_SIZE);
        final int processors = Runtime.getRuntime().availableProcessors();
        System.out.println("amount of processors = " + processors);

        long timer = System.currentTimeMillis();
        System.out.println("Time at start of pool work = " + timer);
        ExecutorService threadPool = Executors.newFixedThreadPool(processors);
        semaphorePoolvsThread.acquire();
        int repeats = totalRepeats;
        while (repeats > 0) {
            summ.set(0.0);
            for (int i = 0; i < processors; i++) {
                int finalI = i;
                Callable<Double> doubleCallable = () -> arrayOps.calculateResult
                        (finalI * (ARRAY_SIZE / processors), (finalI + 1) * ARRAY_SIZE / processors);
                FutureTask<Double> futureTask = new FutureTask<>(doubleCallable);
                threadPool.submit(futureTask);
                summ.set(summ.get() + futureTask.get());
            }
            System.out.println("Total AtomicSumm = " + summ + " from thread pool");
            repeats--;
        }
        semaphorePoolvsThread.release();
        timer -= System.currentTimeMillis();
        System.out.println("pool was working for " + (-1) * timer + "ms");
        threadPool.shutdown();

        semaphorePoolvsThread.acquire();
        Semaphore threadSemaphore = new Semaphore(processors);
        long timer1 = System.currentTimeMillis();
        System.out.println("Time at start of Thread work = " + timer1);
        repeats = totalRepeats;
        while (repeats > 0) {
            summ.set(0.0);
            threadSemaphore.acquire();
            for (int i = 0; i < processors; i++) {
                int finalI = i;
                Callable<Double> threadCallable = () -> arrayOps.calculateResult
                        (finalI * (ARRAY_SIZE / processors), (finalI + 1) * ARRAY_SIZE / processors);
                FutureTask<Double> futureTask = new FutureTask<>(threadCallable);
                new Thread(futureTask).start();
                summ.set(summ.get() + futureTask.get());
            }
            System.out.println("Total AtomicSumm = " + summ + " from threads");
            repeats--;
            threadSemaphore.release();
        }
        semaphorePoolvsThread.release();
        timer1 -= System.currentTimeMillis();
        System.out.println("Threads was working for " + (-1) * timer1 + "ms");

    }
}
