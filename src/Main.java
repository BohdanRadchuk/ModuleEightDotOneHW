import operations.CalculatorOperations;

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
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        Future<Object> future = new FutureTask<Object>(calculateInteger(A, B, operation));
        if (operation.equals("+") || operation.equals("-") || operation.equals("%") || operation.equals("*") || operation.equals("/"))
            future= threadPool.submit(calculateInteger(A, B, operation));
        if (operation.equals("==") || operation.equals("<") || operation.equals(">"))
            future= threadPool.submit(calculateBoolean(A, B, operation));
        System.out.println(future.get());
        threadPool.shutdown();
    }

    public static Callable calculateInteger(int A, int B, String operation){
        CalculatorOperations calculatorOperations = new CalculatorOperations();
        Callable<Number> callable = () -> null;
        if (operation.equals("+"))
            callable = () -> calculatorOperations.plus(A, B);
        if (operation.equals("-"))
            callable = () -> calculatorOperations.odds(A, B);
        if (operation.equals("*"))
            callable = () -> calculatorOperations.multiplication(A, B);
        if (operation.equals("%"))
            callable = () -> calculatorOperations.splitModulo(A, B);
        if (operation.equals("/"))
            callable = () -> calculatorOperations.devide(A, B);

        return callable;
    }


    public static Callable calculateBoolean(int A, int B, String operation) {
        Callable<Boolean> callable = () -> null;
        CalculatorOperations calculatorOperations = new CalculatorOperations();
        if (operation.equals("=="))
            callable = () -> calculatorOperations.equals(A, B);
        if (operation.equals(">"))
            callable = () -> calculatorOperations.bigger(A, B);
        if (operation.equals("<"))
            callable = () -> calculatorOperations.less(A, B);

        return callable;
    }
}
