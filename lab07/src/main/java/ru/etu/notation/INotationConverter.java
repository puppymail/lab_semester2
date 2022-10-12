package ru.etu.notation;

import java.util.Map;
import java.util.Queue;

public interface INotationConverter {

    Map<Character, Integer> OPERANDS = Map.of(
        '+', 1,
        '-', 1,
        '*', 2,
        '/', 2,
        '(', 0,
        ')', 0
    );

    String convert(String exp);

    Queue<String> getTokens();

    static int weightOf(Character symbol) {
        return OPERANDS.getOrDefault(symbol, -1);
    }

    static boolean isOperand(Character symbol) {
        return OPERANDS.containsKey(symbol);
    } 
    
}
