package ru.etu;

import java.util.Queue;
import java.util.Scanner;

import ru.etu.notation.INotationConverter;
import ru.etu.notation.PolishNotationConverter;
import ru.etu.notation.ReversePolishNotationConverter;

public class Lab07 {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Reverse Polish Notation");
        INotationConverter converter = new ReversePolishNotationConverter();
        System.out.println("Please enter expression:");
        String input = SCANNER.nextLine();
        String output = converter.convert(input);
        Queue<String> tokens = converter.getTokens();
        System.out.println("Expression before = '" + input + "'");
        System.out.println("Expression after = '" + output + "'");
        System.out.println("Tokens = " + tokens);
        
        System.out.println("Polish Notation");
        converter = new PolishNotationConverter();
        System.out.println("Please enter expression:");
        input = SCANNER.nextLine();
        output = converter.convert(input);
        tokens = converter.getTokens();
        System.out.println("Expression before = '" + input + "'");
        System.out.println("Expression after = '" + output + "'");
        System.out.println("Tokens = " + tokens);
    }
    
}
