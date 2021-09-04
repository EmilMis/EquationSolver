package empi;

import java.math.BigDecimal;
import java.math.MathContext;

public class Main {
    public static void main(String[] args){
        try {
            String equation = args[0];
            EquationSolver equationSolver = new EquationSolver();
            equationSolver.SolveGivenEquation(equation);
        }
        catch (Exception e){
            System.out.println("Error\nDetails: " + e);
        }
    }
}
