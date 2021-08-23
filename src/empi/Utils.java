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

    public boolean isOperator(char sample){
        char[] operators = {'+', '-', '=', '*', '/', '(', ')'};
        for (char element : operators){
            if (element == sample){
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> fix(ArrayList<String> brokenEquation){
        //3x+12=0
        ArrayList<String> fixedEquation = new ArrayList<String>();
        String part = "";
        for (String element : brokenEquation){
            for (int i = 0; i < element.length(); i++) {
                char digit = element.charAt(i);

                if (isOperator(digit)){
                    if (part != ""){
                        fixedEquation.add(part);
                        part = "";
                    }
                    fixedEquation.add(Character.toString(digit));
                    continue;
                }
                part += digit;
                if (i == element.length() - 1){
                    if (part != ""){
                        fixedEquation.add(part);
                        part = "";
                    }
                }
            }
        }
        boolean isPrevElementNumber = false;
        for (int i = 0; i < fixedEquation.size(); i++) {
            String element = fixedEquation.get(i);
            if (i == 0){
                isPrevElementNumber = IsNumber(element);
                continue;
            }
            boolean isElementNumber = IsNumber(element);
            if (isPrevElementNumber && isElementNumber){
                fixedEquation.set(i - 1, fixedEquation.get(i - 1) + element);
                fixedEquation.remove(i);
                i -= 1;
            }
            isPrevElementNumber = isElementNumber;
        }
        boolean isPrevElementOperator = false;
        for (int i = 0; i < fixedEquation.size(); i++) {
            String element = fixedEquation.get(i);
            if (i == 0){
                isPrevElementOperator = isOperator(element.charAt(0));
                continue;
            }
            boolean isElementOperator = isOperator(element.charAt(0));
            if (isElementOperator && isPrevElementOperator){
                char operator1 = fixedEquation.get(i - 1).charAt(0);
                char operator2 = fixedEquation.get(i).charAt(0);
                if ((operator1 == '+' || operator1 == '-') && (operator2 == '+' || operator2 == '-')){
                    if (operator1 == '+'){
                        fixedEquation.set(i - 1, Character.toString(operator2));
                        fixedEquation.remove(i);
                        i -= 1;
                    }
                    else{
                        if (operator2 == '-'){
                            fixedEquation.set(i - 1, Character.toString('+'));
                            fixedEquation.remove(i);
                            i -= 1;
                        }
                        else{
                            fixedEquation.set(i - 1, Character.toString('-'));
                            fixedEquation.remove(i);
                            i -= 1;
                        }
                    }
                }
                else if ((operator1 == '*' || operator1 == '/') && (operator2 == '*' || operator2 == '/')){
                    if (operator1 == operator2){
                        fixedEquation.remove(i);
                        i -= 1;
                    }
                    else{
                        fixedEquation.remove(i - 1);
                        fixedEquation.remove(i - 1);
                        i -= 2;
                    }
                }
                else if (operator1 == operator2){
                    if (operator1 == '(' || operator1 == ')'){
                        continue;
                    }
                    else {
                        fixedEquation.remove(i - 1);
                        i -= 1;
                    }
                }
            }
            isPrevElementOperator = isElementOperator;
        }
        return fixedEquation;
    }

    public ArrayList<String> simplifyParentheses(ArrayList<String> fixedEquation){
        //quickly check if there are parentheses
        System.out.println(fixedEquation);
        boolean areThereParentheses = false;
        for (String element : fixedEquation){
            if (element.equals("(") || element.equals(")")){
                areThereParentheses = true;
                break;
            }
        }
        if (!areThereParentheses){
            return fixedEquation;
        }

        int parOpen = 0;

        ArrayList<ArrayList<String>> parenthesesExpressions = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<Integer>> parenthesesIndexes = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < fixedEquation.size(); i++){
            String element = fixedEquation.get(i);
            if (element.equals("(")){
                parenthesesExpressions.add(new ArrayList<String>());
                parenthesesIndexes.add(new ArrayList<Integer>());
                parOpen ++;
                parenthesesExpressions.get(parenthesesExpressions.size() - 1).add(element);
                parenthesesIndexes.get(parenthesesIndexes.size() - 1).add(i);
                for (int j = i + 1; j < fixedEquation.size(); j++) {
                    String element2 = fixedEquation.get(j);
                    System.out.println(element2);
                    parenthesesExpressions.get(parenthesesExpressions.size() - 1).add(element2);
                    parenthesesIndexes.get(parenthesesIndexes.size() - 1).add(j);
                    if (element2.equals(")")){
                        parOpen --;
                    }
                    if (parOpen == 0) {
                        System.out.println("\n\n");
                        break;
                    }
                    if (element2.equals("(")){
                        parOpen ++;
                    }
                    i = j;
                }
            }
        }
        System.out.println(parenthesesExpressions);
        System.out.println(parenthesesIndexes);
        for (int i = 0; i < parenthesesIndexes.size(); i++){
            for (int j = 0; j < parenthesesIndexes.get(i).size(); j++) {
                int index = parenthesesIndexes.get(i).get(j);
                fixedEquation.set(index, "none");
            }
        }



        return new ArrayList<String>();
    }

    public ArrayList<String> solveParentheses(ArrayList<String> parenthesesExp){
        ArrayList<String> result = parenthesesExp;
        System.out.println(result);
        result.remove(0);
        result.remove(result.size() - 1);
        result = neatify(result);
        System.out.println(result);
        boolean containsParentheses = false;
        for (String element : result){
            if (element.equals("(") || element.equals(")")){
                containsParentheses = true;
                break;
            }
        }

        int returnValue = 0;
        int returnXValue = 0;
        for (String element : result){
            if (isX(element)){
                if (element.length() < 3) {
                    if (element.charAt(0) == '-') {
                        returnXValue --;
                    }
                    else{
                        returnXValue ++;
                    }
                }
                else{
                    int xValue = Integer.parseInt(element.substring(1, element.length() - 1));
                    if (element.charAt(0) == '+') {
                        returnXValue += xValue;
                    }
                    else{
                        returnXValue -= xValue;
                    }
                }
                continue;
            }
            returnValue += StringToLong(element);
        }
        ArrayList<String> retValue = new ArrayList<String>();
        if (returnValue < 0) {
            retValue.add(Integer.toString(returnValue));
        }else{
            retValue.add("+" + Integer.toString(returnValue));
        }
        if (returnXValue < 0) {
            retValue.add(Integer.toString(returnXValue) + "x");
        }else{
            retValue.add("+" + Integer.toString(returnXValue) + "x");
        }

        return retValue;
    }

    public ArrayList<String> neatify(ArrayList<String> eq){
        ArrayList<String> equation = eq;
        for (int i = 0; i < equation.size(); i++) {
            String element = equation.get(i);
            if (IsNumber(element)){
                if (i == 0){
                    equation.set(i, "+" + element);
                    i -= 1;
                }
                else if (!equation.get(i - 1).equals("=")  && !equation.get(i - 1).equals("*") && !equation.get(i - 1).equals("/") && !equation.get(i - 1).equals("(") && !equation.get(i - 1).equals(")")){
                    equation.set(i - 1, equation.get(i - 1) + element);
                    equation.remove(i);
                    i -= 1;
                }
                else{
                    equation.set(i, "+" + element);
                    i -= 1;
                }
            }
        }
        System.out.println(equation);
        for (int i = 0; i < equation.size(); i++){
            String part = equation.get(i);
            if (part.equals("*") || part.equals("/")){
                String solved = solveMini(equation.get(i - 1), equation.get(i + 1), part);
                equation.set(i, solved);
                equation.remove(i + 1);
                equation.remove(i - 1);
                i -= 1;
            }
        }
        return equation;
    }

    public String solveMini(String firstNumber, String secondNumber, String operation){
        String processedFirstNumber = firstNumber.substring(1, firstNumber.length());
        String processedSecondNumber = secondNumber.substring(1, secondNumber.length());
        if (isX(processedFirstNumber) || isX(processedSecondNumber)){
            if (isX(processedFirstNumber) && isX(processedSecondNumber)){
                return "none";
            }
            else if (isX(processedSecondNumber)){
                if (processedSecondNumber.length() == 1){
                    return firstNumber + "x";
                }
                String SecondNumberWithoutX = secondNumber.substring(0, secondNumber.length() - 1);
                long result;
                if (operation.equals("*")){
                    result = StringToLong(firstNumber) * StringToLong(SecondNumberWithoutX);
                }
                else{
                    result = StringToLong(firstNumber) / StringToLong(SecondNumberWithoutX);
                }
                if (result > 0){
                    return "+" + Long.toString(result) + "x";
                }
                return Long.toString(result) + "x";
            }
            else{
                if (processedFirstNumber.length() == 1){
                    return secondNumber + "x";
                }
                String FirstNumberWithoutX = firstNumber.substring(0, firstNumber.length() - 1);
                long result;
                if (operation.equals("*")){
                    result = StringToLong(FirstNumberWithoutX) * StringToLong(secondNumber);
                }else{
                    result = StringToLong(FirstNumberWithoutX) / StringToLong(secondNumber);
                }
                if (result > 0){
                    return "+" + Long.toString(result) + "x";
                }
                return Long.toString(result) + "x";
            }
        }
        long firstNumberLong = StringToLong(firstNumber);
        long secondNumberLong = StringToLong(secondNumber);
        if (operation.equals("*")){
            long result = firstNumberLong * secondNumberLong;
            if (result > 0){
                return "+" + Long.toString(result);
            }
            return Long.toString(result);
        }else if (operation.equals("/")){
            long result2 = firstNumberLong / secondNumberLong;
            if (result2 > 0){
                return "+" + Long.toString(result2);
            }
            return Long.toString(result2);
        }else{
            return "invalid operation";
        }
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

    public float solve(ArrayList<String> equation){
        long[] numbers = getNumbers(equation);
        long[] xs = getXs(equation);
        long leftSide = xs[0] - xs[1];
        long rightSide = numbers[1] - numbers[0];
        return (float) rightSide / leftSide;
    }

    public long[] getXs(ArrayList<String> equation){
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
        ArrayList<ArrayList<Long>> numberOfXs = new ArrayList<ArrayList<Long>>();
        numberOfXs.add(new ArrayList<Long>());
        numberOfXs.add(new ArrayList<Long>());
        for (String element : xs.get(0)){
            String processedElement = element.substring(0, element.length() - 1);
            if (processedElement.equals("+")){
                numberOfXs.get(0).add(1L);
                continue;
            }
            if (processedElement.equals("-")){
                numberOfXs.get(0).add(-1L);
                continue;
            }
            numberOfXs.get(0).add(StringToLong(processedElement));
        }
        for (String element : xs.get(1)){
            String processedElement = element.substring(0, element.length() - 1);
            if (processedElement.equals("+")){
                numberOfXs.get(1).add(1L);
                continue;
            }
            if (processedElement.equals("-")){
                numberOfXs.get(1).add(-1L);
                continue;
            }
            numberOfXs.get(1).add(StringToLong(processedElement));
        }
        long leftSide = 0L;
        long rightSide = 0L;
        for (double element : numberOfXs.get(0)){
            leftSide += element;
        }
        for (double element : numberOfXs.get(1)){
            rightSide += element;
        }
        numberOfXs.clear();
        return new long[] {leftSide, rightSide};
    }

    public long[] getNumbers(ArrayList<String> equation){
        ArrayList<ArrayList<Long>> result = new ArrayList<ArrayList<Long>>();
        result.add(new ArrayList<Long>());
        for (String element : equation) {
            if (element.equals("=")) {
                result.add(new ArrayList<Long>());
                continue;
            }
            if (isX(element)){
                continue;
            }
            result.get(result.size() - 1).add(StringToLong(element));
        }

        long leftSide = 0L;
        long rightSide = 0L;

        for (int i = 0; i < result.get(0).size(); i++) {
            leftSide += result.get(0).get(i);
        }

        for (int i = 0; i < result.get(1).size(); i++) {
            rightSide += result.get(1).get(i);
        }

        result.clear();

        return new long[] {leftSide, rightSide};
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

    public long StringToLong(String number){
        char status = number.charAt(0);
        String processedNumber = number.substring(1, number.length());
        long rezult = Long.parseLong(processedNumber);
        if (status == '-'){
            return -rezult;
        }
        else{
            return rezult;
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
