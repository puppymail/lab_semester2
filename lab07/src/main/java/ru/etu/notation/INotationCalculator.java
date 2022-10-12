package ru.etu.notation;

import java.util.Map;

public interface INotationCalculator {

    String DEFAULT_FILE_NAME = "calculator-log.txt";

    Double calculate(String exp, boolean verbose, boolean writeToFile);

    Double calculate(String exp, boolean verbose, boolean writeToFile, Map<String, Double> variables);

}
