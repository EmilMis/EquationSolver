package empi;

import java.math.MathContext;
import java.util.ArrayList;
import java.util.Scanner;
import java.math.BigDecimal;

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

        for (int i = 0; i < fixedEquation.size(); i++) {
            String element = fixedEquation.get(i);
            if (i == 0){
                continue;
            }
            String prevElement = fixedEquation.get(i - 1);
            if (element.equals("(")){
                if (IsNumber(prevElement)){
                    fixedEquation.add(i, "*");
                    i ++;
                }
            }
            else if (prevElement.equals(")")){
                if (IsNumber(element)){
                    fixedEquation.add(i, "*");
                    i ++;
                }
            }
        }

        return fixedEquation;
    }

    public ArrayList<String> simplifyParentheses(ArrayList<String> fixedEquation){
        for (int i = 0; i < fixedEquation.size(); i++){
            String element = fixedEquation.get(i);
            if (element.equals("(")){
                break;
            }
            if (i == fixedEquation.size() - 1){
                return fixedEquation;
            }
        }

        ArrayList<ArrayList<String>> parenthesesExp = new ArrayList<ArrayList<String>>();

        int parOpen = 0;

        for (int i = 0; i < fixedEquation.size(); i++) {
            String element = fixedEquation.get(i);
            if (element.equals("(")){
                parenthesesExp.add(new ArrayList<String>());
                for (int j = i; j < fixedEquation.size(); j++) {
                    String parElement = fixedEquation.get(j);
                    if (parElement.equals(")")){
                        parOpen --;
                    }

                    if (parElement.equals("(")){
                        parOpen ++;
                    }

                    parenthesesExp.get(parenthesesExp.size() - 1).add(parElement);
                    if (j == i){
                        fixedEquation.set(j, "none!");
                    }
                    else{
                        fixedEquation.set(j, "none");
                    }
                    if (parOpen == 0){
                        i = j;
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < fixedEquation.size(); i++){
            String element = fixedEquation.get(i);
            if (element.equals("none")){
                fixedEquation.remove(i);
                i --;
            }
        }

        ArrayList<ArrayList<ArrayList<String>>> mults = new ArrayList<ArrayList<ArrayList<String>>>();

        for (int i = 0; i < fixedEquation.size(); i++) {
            String element = fixedEquation.get(i);
            if (element.equals("none!")){
                ArrayList<ArrayList<String>> currnetMults = new ArrayList<ArrayList<String>>();
                currnetMults.add(new ArrayList<String>());
                for (int j = i - 1; j > 0; j -= 2) {
                    String mult = fixedEquation.get(j);
                    if (mult.equals("*") || mult.equals("/")){
                        currnetMults.get(currnetMults.size() - 1).add(0, mult);
                        currnetMults.get(currnetMults.size() - 1).add(0, fixedEquation.get(j - 1));
                        if (j == i - 1){
                            fixedEquation.set(j, "empty!");
                        }
                        else{
                            fixedEquation.set(j, "empty");
                        }
                        fixedEquation.set(j - 1, "empty");
                    }
                    else{
                        break;
                    }
                }
                currnetMults.add(new ArrayList<String>());
                for (int j = i + 1; j < fixedEquation.size(); j += 2) {
                    String mult = fixedEquation.get(j);
                    if (mult.equals("*") || mult.equals("/")){
                        currnetMults.get(currnetMults.size() - 1).add(mult);
                        currnetMults.get(currnetMults.size() - 1).add(fixedEquation.get(j + 1));
                        if (j == i + 1){
                            fixedEquation.set(j, "empty!");
                        }
                        else{
                            fixedEquation.set(j, "empty");
                        }
                        fixedEquation.set(j + 1, "empty");
                    }
                    else{
                        break;
                    }
                }
                mults.add(currnetMults);
            }
        }

        for (int i = 0; i < fixedEquation.size(); i++) {
            String element = fixedEquation.get(i);
            if (element.equals("empty")){
                fixedEquation.remove(i);
                i --;
            }
        }

        for (int i = 0; i < mults.size(); i++) {
            for (int j = 0; j < mults.get(i).size(); j++) {
                String operator = "";
                ArrayList<String> multLine = mults.get(i).get(j);
                if (multLine.isEmpty()){
                    continue;
                }
                if (multLine.get(0).equals("*") || multLine.get(0).equals("/")){
                    operator = " " + multLine.get(0);
                    multLine.remove(0);
                }
                if (multLine.get(multLine.size() - 1).equals("*") || multLine.get(multLine.size() - 1).equals("/")){
                    operator = multLine.get(multLine.size() - 1) + " ";
                    multLine.remove(multLine.size() - 1);
                }

                multLine = neatify(multLine);

                if (operator.charAt(0) == ' '){
                    multLine.add(0, Character.toString(operator.charAt(1)));
                }
                else{
                    multLine.add(Character.toString(operator.charAt(0)));
                }

            }
        }

        for (int i = 0; i < parenthesesExp.size(); i++) {
            ArrayList<String> parenthesesEx = parenthesesExp.get(i);
            parenthesesEx = solveParentheses(parenthesesEx);
            parenthesesExp.set(i, parenthesesEx);
        }


        int counter = 0;

        for (int i = 0; i < fixedEquation.size(); i++) {
            String element = fixedEquation.get(i);
            if (element.equals("none!")) {
                counter++;
                ArrayList<String> parExp = parenthesesExp.get(counter - 1);
                ArrayList<ArrayList<String>> currentMults = mults.get(counter - 1);

                if (currentMults.get(0).isEmpty() && currentMults.get(1).isEmpty()) {
                    fixedEquation.set(i, parExp.get(1));
                    fixedEquation.add(i, parExp.get(0));
                    if (fixedEquation.get(i - 1).equals("-")) {
                        fixedEquation.set(i, solveMini(fixedEquation.get(i), "-1", "*"));
                        fixedEquation.set(i + 1, solveMini(fixedEquation.get(i + 1), "-1", "*"));
                    }
                    fixedEquation.remove(i - 1);
                    continue;
                }

                if (currentMults.get(1).isEmpty()){
                    String mult;
                    if (i - 1 == '-'){
                        mult = "-1";
                    }
                    else{
                        mult = "1";
                    }
                    if (fixedEquation.get(i - 1).equals("+") || fixedEquation.get(i - 1).equals("-")) {
                        fixedEquation.remove(i - 1);
                        i--;
                    }
                    fixedEquation.add(i + 1, solveMini(parExp.get(1), mult, "*"));
                    fixedEquation.add(i + 1, currentMults.get(0).get(1));
                    fixedEquation.add(i + 1, currentMults.get(0).get(0));
                    fixedEquation.set(i, solveMini(parExp.get(0), mult, "*"));
                    fixedEquation.add(i, currentMults.get(0).get(1));
                    fixedEquation.set(i - 1, currentMults.get(0).get(0));

                } else if (currentMults.get(0).isEmpty()) {
                    String mult;
                    if (i - 1 == '-'){
                        mult = "-1";
                    }
                    else{
                        mult = "1";
                    }
                    if (fixedEquation.get(i - 1).equals("+") || fixedEquation.get(i - 1).equals("-")) {
                        fixedEquation.remove(i - 1);
                        i--;
                    }
                    fixedEquation.set(i + 1, currentMults.get(1).get(1));
                    fixedEquation.add(i + 1, currentMults.get(1).get(0));
                    fixedEquation.set(i, solveMini(parExp.get(1), mult, "*"));
                    fixedEquation.add(i, currentMults.get(1).get(1));
                    fixedEquation.add(i, currentMults.get(1).get(0));
                    fixedEquation.add(i, solveMini(parExp.get(0), mult, "*"));

                } else {
                    String mult;
                    if (i - 1 == '-'){
                        mult = "-1";
                    }
                    else{
                        mult = "1";
                    }
                    if (fixedEquation.get(i - 1).equals("+") || fixedEquation.get(i - 1).equals("-")) {
                        fixedEquation.remove(i - 1);
                        i--;
                    }
                    fixedEquation.set(i + 1, currentMults.get(1).get(1));
                    fixedEquation.add(i + 1, currentMults.get(1).get(0));
                    fixedEquation.set(i, solveMini(parExp.get(0), mult, "*"));
                    fixedEquation.set(i - 1, currentMults.get(0).get(1));
                    fixedEquation.add(i - 1, currentMults.get(0).get(0));
                    fixedEquation.add(i + 4, currentMults.get(1).get(1));
                    fixedEquation.add(i + 4, currentMults.get(1).get(0));
                    fixedEquation.add(i + 4, solveMini(parExp.get(1), mult, "*"));
                    fixedEquation.add(i + 4, currentMults.get(0).get(1));
                    fixedEquation.add(i + 4, currentMults.get(0).get(0));
                }
            }
        }

        return fixedEquation;
    }

    public ArrayList<String> solveParentheses(ArrayList<String> parenthesesExp){
        ArrayList<String> result = parenthesesExp;
        result.remove(0);
        result.remove(result.size() - 1);
        boolean containsParentheses = false;
        for (String element : result){
            if (element.equals("(") || element.equals(")")){
                containsParentheses = true;
                break;
            }
        }
        if (containsParentheses){
            result = simplifyParentheses(result);
        }
        result = neatify(result);

        BigDecimal returnValue = new BigDecimal("0");
        BigDecimal returnXValue = new BigDecimal("0");
        for (String element : result){
            if (isX(element)) {
                if (element.length() < 3) {
                    if (element.charAt(0) == '-') {
                        returnXValue = returnXValue.subtract(new BigDecimal("1"), new MathContext(returnXValue.precision() + returnXValue.toString().length()));
                    } else {
                        returnXValue = returnXValue.add(new BigDecimal("1"), new MathContext(returnXValue.precision() + returnXValue.toString().length()));
                    }
                } else {
                    BigDecimal xValue = new BigDecimal(element.substring(1, element.length() - 1));
                    if (element.charAt(0) == '+') {
                        returnXValue = returnXValue.add(xValue, new MathContext(Math.max(returnXValue.precision() + returnXValue.toString().length(), xValue.precision() + xValue.toString().length())));
                    } else {
                        returnXValue = returnXValue.subtract(xValue, new MathContext(Math.max(returnXValue.precision() + returnXValue.toString().length(), xValue.precision() + xValue.toString().length())));
                    }
                }
                continue;
            }
            BigDecimal elementBg = new BigDecimal(element);
            returnValue = returnValue.add(elementBg, new MathContext(Math.max(returnValue.precision() + returnValue.toString().length(), elementBg.precision() + elementBg.toString().length())));
        }
        ArrayList<String> retValue = new ArrayList<String>();
        if (returnValue.compareTo(new BigDecimal("0")) < 0) {
            retValue.add(returnValue.toString());
        }else{
            retValue.add("+" + returnValue.toString());
        }
        if (returnXValue.compareTo(new BigDecimal("0")) < 0) {
            retValue.add(returnXValue.toString() + "x");
        }else{
            retValue.add("+" + returnXValue.toString() + "x");
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

        equation = simplifyParentheses(equation);

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
                if (processedFirstNumber.length() == 1){
                    processedFirstNumber = "1";
                }else{
                    processedFirstNumber = processedFirstNumber.substring(0, processedFirstNumber.length() - 1);
                }
                if (processedSecondNumber.length() == 1){
                    processedSecondNumber = "1";
                }
                else{
                    processedSecondNumber = processedSecondNumber.substring(0, processedSecondNumber.length() - 1);
                }

                if (operation.equals("*")){
                    if (processedSecondNumber.equals("0") || processedFirstNumber.equals("0")){
                        return "+0";
                    }
                    else{
                        return "none";
                    }
                }

                if (operation.equals("/")){
                    String solved = Long.toString(Long.parseLong(processedFirstNumber) / Long.parseLong(processedSecondNumber));
                    if ((firstNumber.charAt(0) == '+' && secondNumber.charAt(0) == '+') || (firstNumber.charAt(0) == '-' && secondNumber.charAt(0) == '-')){
                        return "+" + solved;
                    }
                    else{
                        return "-" + solved;
                    }
                }
                return "invalid";
            }
            if (isX(processedSecondNumber)){
                if (processedSecondNumber.length() == 1){
                    return firstNumber + "x";
                }
                String SecondNumberWithoutX = secondNumber.substring(0, secondNumber.length() - 1);
                BigDecimal result;
                BigDecimal firstNumberBg = new BigDecimal(firstNumber);
                BigDecimal SecondNumberWithoutXBg = new BigDecimal(SecondNumberWithoutX);
                if (operation.equals("*")){
                    result = firstNumberBg.multiply(SecondNumberWithoutXBg, new MathContext(Math.max(firstNumberBg.precision() + firstNumberBg.toString().length(), SecondNumberWithoutXBg.precision() + SecondNumberWithoutX.toString().length())));
                }
                else{
                    result = firstNumberBg.divide(SecondNumberWithoutXBg, new MathContext(Math.max(firstNumberBg.precision() + firstNumberBg.toString().length(), SecondNumberWithoutXBg.precision() + SecondNumberWithoutXBg.toString().length())));
                }
                if (result.compareTo(new BigDecimal("-1")) > 0){
                    return "+" + result.toString() + "x";
                }
                return result.toString() + "x";
            }
            else{
                if (processedFirstNumber.length() == 1){
                    return secondNumber + "x";
                }
                String FirstNumberWithoutX = firstNumber.substring(0, firstNumber.length() - 1);
                BigDecimal FirstNumberWithoutXBg = new BigDecimal(FirstNumberWithoutX);
                BigDecimal secondNumberBg = new BigDecimal(secondNumber);
                BigDecimal result;
                int max = Math.max(FirstNumberWithoutXBg.precision() + FirstNumberWithoutXBg.toString().length(), secondNumberBg.precision() + secondNumberBg.toString().length());
                MathContext mc = new MathContext(Math.max(FirstNumberWithoutXBg.precision() + FirstNumberWithoutXBg.toString().length(), secondNumberBg.precision() + secondNumberBg.toString().length()));
                if (operation.equals("*")){
                    result = FirstNumberWithoutXBg.multiply(secondNumberBg, new MathContext(Math.max(FirstNumberWithoutXBg.precision() + FirstNumberWithoutXBg.toString().length(), secondNumberBg.precision() + secondNumberBg.toString().length())));
                }else{
                    result = FirstNumberWithoutXBg.divide(secondNumberBg, new MathContext(Math.max(FirstNumberWithoutXBg.precision() + FirstNumberWithoutXBg.toString().length(), secondNumberBg.precision() + secondNumberBg.toString().length())));
                }
                if (result.compareTo(new BigDecimal("-1")) > 0){
                    return "+" + result.toString() + "x";
                }
                return result.toString() + "x";
            }
        }
        BigDecimal firstNumberBg = new BigDecimal(firstNumber);
        BigDecimal secondNumberBg = new BigDecimal(secondNumber);
        BigDecimal result;
        if (operation.equals("*")){
            result = firstNumberBg.multiply(secondNumberBg, new MathContext(Math.max(firstNumberBg.precision() + firstNumberBg.toString().length(), secondNumberBg.precision() + secondNumberBg.toString().length())));
            if (result.compareTo(new BigDecimal("-1")) > 0){
                return "+" + result.toString();
            }
            return result.toString();
        }else if (operation.equals("/")){
            result = firstNumberBg.divide(secondNumberBg, new MathContext(Math.max(firstNumberBg.precision() + firstNumberBg.toString().length(), secondNumberBg.precision() + secondNumberBg.toString().length())));
            if (result.compareTo(new BigDecimal("-1")) > 0){
                return "+" + result.toString();
            }
            return result.toString();
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

    public String solve(ArrayList<String> equation){
        String[] numbers = getNumbers(equation);
        String[] xs = getXs(equation);
        BigDecimal xsBg0= new BigDecimal(xs[0]);
        BigDecimal xsBg1= new BigDecimal(xs[1]);
        BigDecimal numbersBg0= new BigDecimal(numbers[0]);
        BigDecimal numbersBg1= new BigDecimal(numbers[1]);

        BigDecimal leftSide = xsBg0.subtract(xsBg1, new MathContext(Math.max(xsBg0.precision() + xsBg0.toString().length(), xsBg1.precision() + xsBg1.toString().length())));
        BigDecimal rightSide = numbersBg1.subtract(numbersBg0, new MathContext(Math.max(numbersBg0.precision() + numbersBg0.toString().length(), numbersBg1.precision() + numbersBg1.toString().length())));

        return  rightSide.divide(leftSide, new MathContext(Math.max(rightSide.precision() + rightSide.toString().length(), leftSide.precision() + leftSide.toString().length()))).toString();
    }

    public String[] getXs(ArrayList<String> equation){
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
        ArrayList<ArrayList<String>> numberOfXs = new ArrayList<ArrayList<String>>();
        numberOfXs.add(new ArrayList<String>());
        numberOfXs.add(new ArrayList<String>());
        for (String element : xs.get(0)){
            String processedElement = element.substring(0, element.length() - 1);
            if (processedElement.equals("+")){
                numberOfXs.get(0).add("1");
                continue;
            }
            if (processedElement.equals("-")){
                numberOfXs.get(0).add("-1");
                continue;
            }
            numberOfXs.get(0).add(processedElement);
        }
        for (String element : xs.get(1)){
            String processedElement = element.substring(0, element.length() - 1);
            if (processedElement.equals("+")){
                numberOfXs.get(1).add("1");
                continue;
            }
            if (processedElement.equals("-")){
                numberOfXs.get(1).add("-1");
                continue;
            }
            numberOfXs.get(1).add(processedElement);
        }
        BigDecimal leftSide = new BigDecimal("0");
        BigDecimal rightSide = new BigDecimal("0");
        for (String element : numberOfXs.get(0)){
            BigDecimal elementBg = new BigDecimal(element);
            leftSide = leftSide.add(elementBg, new MathContext(Math.max(leftSide.precision() + leftSide.toString().length(), elementBg.precision() + elementBg.toString().length())));
        }
        for (String element : numberOfXs.get(1)){
            BigDecimal elementBg = new BigDecimal(element);
            rightSide = rightSide.add(elementBg, new MathContext(Math.max(rightSide.precision() + rightSide.toString().length(), elementBg.precision() + elementBg.toString().length())));
        }
        numberOfXs.clear();
        return new String[] {leftSide.toString(), rightSide.toString()};
    }

    public String[] getNumbers(ArrayList<String> equation){
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        result.add(new ArrayList<String>());
        for (String element : equation) {
            if (element.equals("=")) {
                result.add(new ArrayList<String>());
                continue;
            }
            if (isX(element)){
                continue;
            }
            result.get(result.size() - 1).add(element);
        }

        BigDecimal leftSide = new BigDecimal("0");
        BigDecimal rightSide = new BigDecimal("0");

        for (int i = 0; i < result.get(0).size(); i++) {
            BigDecimal element = new BigDecimal(result.get(0).get(i));
            leftSide = leftSide.add(element, new MathContext(Math.max(element.precision() + element.toString().length(), leftSide.precision() + leftSide.toString().length())));
        }

        for (int i = 0; i < result.get(1).size(); i++) {
            BigDecimal element = new BigDecimal(result.get(1).get(i));
            rightSide = rightSide.add(element, new MathContext(Math.max(element.precision() + element.toString().length(), rightSide.precision() + rightSide.toString().length())));
        }

        result.clear();

        return new String[] {leftSide.toString(), rightSide.toString()};
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
