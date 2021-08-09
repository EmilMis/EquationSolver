package empi;

import java.util.ArrayList;
import java.util.Scanner;

public class Utils {
    public String getEquationRaw() {
        Scanner scanner = new Scanner(System.in);
        String eq;
        eq = scanner.nextLine();
        return eq;
    }

    public ArrayList<String> convertEquationToArrayList(String equation) {
        ArrayList<String> eq = new ArrayList<String>();
        String element = "";
        for (int i = 0; i < equation.length(); i++) {
            char digit = equation.charAt(i);
            if (digit == ' ') {
                if (element != "") {
                    eq.add(element);
                    element = "";
                    continue;
                }
                continue;
            }
            element += digit;
            if (i == equation.length() - 1) {
                eq.add(element);
                element = "";
            }
        }
        return eq;
    }

    public ArrayList<String> neatify(ArrayList<String> eq){
        ArrayList<String> equation = eq;
        for (int i = 0; i < equation.size(); i++) {
            String element = equation.get(i);
            if (IsNumber(element)){
                if (i == 0){
                    equation.set(i, "+" + element);
                    i = 0;
                }
                else if (!equation.get(i - 1).equals("=")){
                    equation.set(i - 1, equation.get(i - 1) + element);
                    equation.remove(i);
                    i = 0;
                }
                else{
                    equation.set(i, "+" + element);
                    i = 0;
                }
            }
        }
        return equation;
    }

    public boolean IsNumber(String sample){
        char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'x'};
        for (char digit : digits) {
            if (sample.charAt(0) == digit) {
                return true;
            }
        }
        return false;
    }

    public double solve(ArrayList<String> equation){
        int[] numbers = getNumbers(equation);
        int[] xs = getXs(equation);
        double leftSide = xs[0] - xs[1];
        double rightSide = numbers[1] - numbers[0];
        return rightSide / leftSide;
    }

    public int[] getXs(ArrayList<String> equation){
        ArrayList<ArrayList<String>> xs = new ArrayList<ArrayList<String>>();
        xs.add(new ArrayList<String>());
        for (String element : equation){
            if (element.equals("=")){
                xs.add(new ArrayList<String>());
                continue;
            }
            if (!isX(element)){
                continue;
            }
            xs.get(xs.size() - 1).add(element);
        }
        ArrayList<ArrayList<Integer>> numberOfXs = new ArrayList<ArrayList<Integer>>();
        numberOfXs.add(new ArrayList<Integer>());
        numberOfXs.add(new ArrayList<Integer>());
        for (String element : xs.get(0)){
            String processedElement = element.substring(0, element.length() - 1);
            if (processedElement.equals("+")){
                numberOfXs.get(0).add(1);
                continue;
            }
            if (processedElement.equals("-")){
                numberOfXs.get(0).add(-1);
                continue;
            }
            numberOfXs.get(0).add(StringToInt(processedElement));
        }
        for (String element : xs.get(1)){
            String processedElement = element.substring(0, element.length() - 1);
            if (processedElement.equals("+")){
                numberOfXs.get(1).add(1);
                continue;
            }
            if (processedElement.equals("-")){
                numberOfXs.get(1).add(-1);
                continue;
            }
            numberOfXs.get(1).add(StringToInt(processedElement));
        }
        int leftSide = 0;
        int rightSide = 0;
        for (int element : numberOfXs.get(0)){
            leftSide += element;
        }
        for (int element : numberOfXs.get(1)){
            rightSide += element;
        }
        return new int[] {leftSide, rightSide};
    }

    public int[] getNumbers(ArrayList<String> equation){
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        result.add(new ArrayList<Integer>());
        for (String element : equation) {
            if (element.equals("=")) {
                result.add(new ArrayList<Integer>());
                continue;
            }
            if (isX(element)){
                continue;
            }
            result.get(result.size() - 1).add(StringToInt(element));
        }

        int leftSide = 0;
        int rightSide = 0;

        for (int i = 0; i < result.get(0).size(); i++) {
            leftSide += result.get(0).get(i);
        }

        for (int i = 0; i < result.get(1).size(); i++) {
            rightSide += result.get(1).get(i);
        }

        result.clear();

        return new int[] {leftSide, rightSide};
    }

    public boolean isX(String sample){
        char[] allowedLetters = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '+', '-'};
        for (int i = 0; i < sample.length(); i++) {
            char letter = sample.charAt(i);
            if (!doesContain(allowedLetters, letter)){
                return true;
            }
        }
        return false;
    }

    public boolean doesContain(char[] list, char element){
        for (char part : list){
            if (part == element){
                return true;
            }
        }
        return false;
    }

    public int StringToInt(String number){
        return (int) StringToDouble(number);
    }

    public double StringToDouble(String number){
        char status = number.charAt(0);
        String AbsoluteNumber = number.substring(1, number.length());
        ArrayList<Integer> digits = new ArrayList<Integer>();

        for (int i = 0; i < AbsoluteNumber.length(); i++) {
            char digit = AbsoluteNumber.charAt(i);
            digits.add(charToInT(digit));
        }

        double result = 0;
        for (int i = 0; i < digits.size(); i++) {
            int digit = digits.get(i);
            result += (toPower(10, digits.size() - 1 - i) * digit);
        }

        if (status == '-'){
            return -result;
        }
        else{
            return result;
        }
    }

    public int toPower(int number, int power){
        int result = 1;

        for (int i = 0; i < power; i++) {
            result *= number;
        }

        return result;
    }

    public int charToInT(char digit){
        return switch(digit){
            case '0' -> 0;
            case '1' -> 1;
            case '2' -> 2;
            case '3' -> 3;
            case '4' -> 4;
            case '5' -> 5;
            case '6' -> 6;
            case '7' -> 7;
            case '8' -> 8;
            case '9' -> 9;
            default -> -1;
        };
    }


}
