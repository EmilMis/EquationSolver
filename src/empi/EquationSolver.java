package empi;

import java.util.ArrayList;
import java.util.Random;

public class EquationSolver {
    public void SolveGivenEquation(String equation){
        Utils utils = new Utils();
        long startTime = System.nanoTime();
        ArrayList<String> equationArrayList = utils.convertEquationToArrayList(equation);
        ArrayList<String> equationNeat = utils.neatify(equationArrayList);
        double solution = utils.solve(equationNeat);
        long endTime = System.nanoTime();
        System.out.println("Solved in " + ((endTime - startTime) / 1000000) + " milliseconds,\n" + getMessage((endTime - startTime) / 1000000) + "\n");
        long[] numbers = utils.getNumbers(equationNeat);
        long[] xs = utils.getXs(equationNeat);
        System.out.println(equation);
        System.out.println(equationArrayList);
        System.out.println(equationNeat);
        System.out.println(xs[0] + "x + " + numbers[0] + " = " + xs[1] + "x + " + numbers[1]);
        System.out.println((xs[0] - xs[1]) + "x = " + (numbers[1] - numbers[0]));
        System.out.print("x = " + (numbers[1] - numbers[0]) + "/" + (xs[0] - xs[1]) + " = " + solution + " = ");
        System.out.printf("%.0f", solution);
    }
    public void SolveEquation(){
        Utils utils = new Utils();
        System.out.print("Write your equation here >>> ");
        String equation = utils.getEquationRaw();
        SolveGivenEquation(equation);
    }

    private String getMessage(long delay){
        String[] messages = {
                "enough time for me to eat an ice cream",
                "ehough time for a c++ developer to open StackOverflow",
                "time needed to solve this equation (wow)",
                "enough time for light to\ntravel " + Float.toString((float) (299792458 / 1000) * delay) + " meters",
                "enough time for a snail to\ncross " + Float.toString((float) (48000000 / (60 * 60 * 1000)) * delay) + " nanometers",
                "time needed for a cheetah to\ncross " + Float.toString((float) (120000000 / (60 * 60 * 1000)) * delay) + " millimeters\nWhen at full speed, of course",
                "I ran out of ideas to put here",
                "time necessary to find stupid things online",
                "t1me n3eded f0r mY c4T t0 f3ll asl3ep 0n My<<<<< K3yb0ard---------!",
                "way less time necessary to put another good fact here",
                "c++ is faster, though",
                "time needed for Windows to crash",
                "My ccat 1s st1ll H3re-..,,>"
        };
        Random random = new Random();
        return messages[random.nextInt(messages.length)];
    }

}