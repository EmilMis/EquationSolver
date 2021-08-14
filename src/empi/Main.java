package empi;

public class Main {
    public static void main(String[] args) {
        try {
            EquationSolver equationSolver = new EquationSolver();
            equationSolver.SolveEquation();
        }catch (Exception exception){
            if (exception instanceof NumberFormatException){
                System.out.print("n! " + exception +  "|");
            }
            else{
                System.out.print("?! " + exception + "|");
            }
        }
    }
}