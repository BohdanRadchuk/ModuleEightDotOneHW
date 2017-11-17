import operations.ArrayOperations;

import java.util.Scanner;
import java.util.concurrent.*;

public class TaskTwoMain {
    static final int ARRAY_SIZE = 80000;
    static double summ;
    public static void main(String[] args) {
        Semaphore semaphorePoolvsThread = new Semaphore(1);
        ArrayOperations arrayOps = new ArrayOperations();
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many repeats do you want to do?");
        int repeats = scanner.nextInt();
        arrayOps.intArrayCreation(ARRAY_SIZE);

        ExecutorService threadPool = Executors.newFixedThreadPool(4);


        Callable<Integer> callable = new Callable() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        };
        Future<Integer> future = new FutureTask<Integer>(callable);
        future = threadPool.submit(callable);
        final int processors = Runtime.getRuntime().availableProcessors();

        for (int i = 0; i<processors; i++){
            final int count = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    summ += arrayOps.calculateResult(count*(ARRAY_SIZE/processors),(count+1)*ARRAY_SIZE/processors);
                }
            };
        Thread thread = new Thread();
        thread.start();}

    }
}
