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
        int[] numbers = utils.getNumbers(equationNeat);
        int[] xs = utils.getXs(equationNeat);
        System.out.println(equation);
        System.out.println(equationArrayList);
        System.out.println(equationNeat);
        System.out.println(Integer.toString(xs[0]) + "x + " + Integer.toString(numbers[0]) +  "x = "+ Integer.toString(xs[1]) + " + " + Integer.toString(numbers[1]));
        System.out.println(solution);
    }
    public void SolveEquation(){
        Utils utils = new Utils();
        System.out.print("Write your equation here >>> ");
        String equation = utils.getEquationRaw();
        SolveGivenEquation(equation);
    }

}
