package operations;

public class CalculatorOperations {
    public Integer plus(int A, int B){
        return A+B;
    }
    public Integer odds(int A, int B){
        return A-B;
    }
    public Integer multiplication(int A, int B){
        return A*B;
    }
    public Double devide(int A, int B){
        return (double)A/(double)B;
    }
    public Integer splitModulo(int A, int B){
        return A%B;
    }
    public Boolean equals(int A, int B){
        return A==B;
    }
    public Boolean bigger(int A, int B){
        return A>B;
    }
    public Boolean less(int A, int B){
        return A<B;
    }
}
