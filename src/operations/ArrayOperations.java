package operations;

public class ArrayOperations {
    Integer [] intarray;

    public void intArrayCreation(int size){
        intarray = new Integer[size];
        for (int i = 0; i<intarray.length; i++){
            intarray[i] = i+1;
        }
    }
    public double calculateResult (int begin, int end){
        double result = 0;
        for (int i = begin; i < end; i++)
            result += Math.sin(intarray[i]) + Math.cos(intarray[i]);
        return result;
    }
}
