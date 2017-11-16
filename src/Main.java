import java.util.Scanner;
import java.util.concurrent.*;

public class Main<T> {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter two numbers");
        int A = scanner.nextInt();
        int B = scanner.nextInt();
        System.out.println("what operation do you want to do with your numbers?");
        scanner.nextLine();
        String operation = scanner.nextLine();
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        if (operation == "+" || operation == "-" || operation == "%" )
            T = Integer;
        Future<Integer> future; = threadPool.submit(calculate(A, B, operation));
        System.out.println(future.get());
        threadPool.shutdown();

    }
    public static Callable calculate(int A, int B, String operation){
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "You have entered unsupportable operation";
            }
        };
        Operations operations = new Operations();
        if (operation.equals("+"))
            callable = () -> operations.plus(A, B);
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
