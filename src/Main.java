import java.util.Scanner;
import java.util.concurrent.*;

public class Main {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter two numbers");
        int A = scanner.nextInt();
        int B = scanner.nextInt();
        System.out.println("what operation do you want to do with your numbers?");
        scanner.nextLine();
        String operation = scanner.nextLine();
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        Future<Number> future = new FutureTask<Number>(calculateInteger(A, B, operation));
        if (operation.equals("+") || operation.equals("-") || operation.equals("%") || operation.equals("*") || operation.equals("/"))
            future= threadPool.submit(calculateInteger(A, B, operation));
        if (operation.equals("==") || operation.equals("<") || operation.equals(">"))
            future= threadPool.submit(calculateBoolean(A, B, operation));
        System.out.println(future.get());
        threadPool.shutdown();
    }

    public static Callable calculateInteger(int A, int B, String operation){
        Operations operations = new Operations();
        Callable<Number> callable = new Callable<Number>() {
            @Override
            public Integer call() throws Exception {
                return null;
            }
        };
        if (operation.equals("+"))
            callable = () -> operations.plus(A, B);
        if (operation.equals("-"))
            callable = () -> operations.odds(A, B);
        if (operation.equals("*"))
            callable = () -> operations.multiplication(A, B);
        if (operation.equals("%"))
            callable = () -> operations.splitModulo(A, B);
        if (operation.equals("/"))
            callable = () -> operations.devide(A, B);

        return callable;
    }


    public static Callable calculateBoolean(int A, int B, String operation) {
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        };
        Operations operations = new Operations();
        if (operation.equals("=="))
            callable = () -> operations.equals(A, B);
        if (operation.equals(">"))
            callable = () -> operations.bigger(A, B);
        if (operation.equals("<"))
            callable = () -> operations.less(A, B);

        return callable;
    }
}
