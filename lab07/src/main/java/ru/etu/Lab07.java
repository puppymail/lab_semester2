package ru.etu;

import java.util.Queue;
import java.util.Scanner;

import ru.etu.notation.INotationCalculator;
import ru.etu.notation.INotationConverter;
import ru.etu.notation.PolishNotationConverter;
import ru.etu.notation.ReversePolishNotationCalculator;
import ru.etu.notation.PolishNotationCalculator;
import ru.etu.notation.ReversePolishNotationConverter;

public class Lab07 {

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final String SEPARATOR = "-".repeat(20);

    public static void main(String[] args) {
        System.out.println(SEPARATOR);

        System.out.println("Reverse Polish Notation");
        INotationConverter converter = new ReversePolishNotationConverter();
        System.out.println("Please enter expression:");
        String input = SCANNER.nextLine();
        String output = converter.convert(input, true, true);
        Queue<String> tokens = converter.getTokens();
        System.out.println("Expression before = '" + input + "'");
        System.out.println("Expression after = '" + output + "'");
        System.out.println("Tokens = " + tokens);
        System.out.println(SEPARATOR);
        INotationCalculator calculator = new ReversePolishNotationCalculator();
        Double result = calculator.calculate(output, true, true);
        System.out.println("Result of calculation = " + result);
        
        System.out.println(SEPARATOR);

        System.out.println("Polish Notation");
        converter = new PolishNotationConverter();
        System.out.println("Please enter expression:");
        input = SCANNER.nextLine();
        output = converter.convert(input, true, true);
        tokens = converter.getTokens();
        System.out.println("Expression before = '" + input + "'");
        System.out.println("Expression after = '" + output + "'");
        System.out.println("Tokens = " + tokens);
        System.out.println(SEPARATOR);
        calculator = new PolishNotationCalculator();
        result = calculator.calculate(output, true, true);
        System.out.println("Result of calculation = " + result);

        System.out.println(SEPARATOR);
    }
    
}
