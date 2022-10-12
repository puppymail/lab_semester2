package ru.etu.notation;

import java.util.Map;
import java.util.Queue;

public interface INotationConverter {

    String DEFAULT_FILE_NAME = "converter-log.txt";

    Map<Character, Integer> OPERANDS = Map.of(
        '+', 1,
        '-', 1,
        '*', 2,
        '/', 2,
        '(', 0,
        ')', 0
    );

    static int weightOf(Character symbol) {
        return OPERANDS.getOrDefault(symbol, -1);
    }

    static boolean isOperand(Character symbol) {
        return OPERANDS.containsKey(symbol);
    }

    String convert(String exp, boolean verbose, boolean writeToFile);

    Queue<String> getTokens();
    
}
