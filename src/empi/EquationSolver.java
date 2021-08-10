package empi;

import java.util.ArrayList;

public class EquationSolver {
    public void SolveGivenEquation(String equation){
        Utils utils = new Utils();
        long startTime = System.nanoTime();
        ArrayList<String> equationArrayList = utils.convertEquationToArrayList(equation);
        ArrayList<String> equationNeat = utils.neatify(equationArrayList);
        double solution = utils.solve(equationNeat);
        long endTime = System.nanoTime();
        System.out.println("Solved in " + ((endTime - startTime) / 1000000) + " milliseconds");
        long[] numbers = utils.getNumbers(equationNeat);
        long[] xs = utils.getXs(equationNeat);
        System.out.println(equation);
        System.out.println(equationArrayList);
        System.out.println(equationNeat);
        System.out.println(xs[0] + "x + " + numbers[0] +  " = "+ xs[1] + "x + " + numbers[1]);
        System.out.println((xs[0] - xs[1]) + "x = " + (numbers[1] - numbers[0]));
        System.out.println("x = " + solution);
    }
    public void SolveEquation(){
        Utils utils = new Utils();
        System.out.print("Write your equation here >>> ");
        String equation = utils.getEquationRaw();
        SolveGivenEquation(equation);
    }

}
