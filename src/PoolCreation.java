import java.util.concurrent.*;

public class PoolCreation<T> {
    private int A;
    private int B;
    private String operation;

    public PoolCreation(int a, int b, String operation) {
        A = a;
        B = b;
        this.operation = operation;
    }

    public void executor() throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        Future<T> future = threadPool.submit(calculate(A, B, operation));
        System.out.println(future.get());
        threadPool.shutdown();
    }

    public Callable<T> calculate(int A, int B, String operation){
        Callable<T> callable = new Callable<T>() {
            @Override
            public T call() throws Exception {
                return null;
            }
        };
        Operations operations = new Operations();
        if (operation.equals("+"))
            callable = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return operations.plus(A, B);
                }
            };
        if (operation.equals("-"))
            callable = () -> operations.odds(A, B);
        if (operation.equals("*"))
            System.out.println(operations.multiplication(A, B));
        if (operation.equals("/"))
            callable = () -> operations.devide(A, B);
        if (operation.equals("%"))
            callable = () -> operations.splitModulo(A, B);
        if (operation.equals("=="))
            callable = () -> operations.equals(A, B);
        if (operation.equals(">"))
            callable = () -> operations.bigger(A, B);
        if (operation.equals("<"))
            callable = () ->  operations.less(A, B);

        return callable;
    }

}
