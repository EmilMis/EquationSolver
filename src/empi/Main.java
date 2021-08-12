package empi;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("welcome to Equation Solver!\nWritten and debugged by EmilMis\nHave fun!\nOh, and a warning:\nIf you go nuts with numbers (which undoubtedly you will)\nprogram is going to crash because they're to big\nfor a variable to store\n\n\n");
            EquationSolver equationSolver = new EquationSolver();
            equationSolver.SolveGivenEquation(args[0]);
        }catch (Exception exception){
            System.out.print("Program crashed, cause: ");
            if (exception instanceof NumberFormatException){
                System.out.println("you passed a to big number for a variable to hold\n(You were warned about it), details: " + exception);
            }
            else{
                System.out.println("Unknown error, details: " + exception);
            }
        }
    }
}