package ru.etu.notation;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Pattern;
import ru.etu.operation.Operation;

public class ReversePolishNotationCalculator implements INotationCalculator {

    private static final String VAR_REGEXP = "[a-zA-Z]+";

    private static final Pattern VAR_PATTERN = Pattern.compile(VAR_REGEXP);

    private final Queue<String> stack;

    private Path file = Paths.get(DEFAULT_FILE_NAME);

    public ReversePolishNotationCalculator() {
        this.stack = Collections.asLifoQueue(new LinkedList<>());
    }

    public ReversePolishNotationCalculator(String filePath) {
        this();
        this.file = Paths.get(filePath);
    }
    
    @Override
    public Double calculate(String exp, boolean verbose, boolean writeToFile) {
        return this.calculate(exp, verbose, writeToFile, null);
    }

    @Override
    public Double calculate(String exp, final boolean verbose, final boolean writeToFile, Map<String, Double> variables) {
        stack.clear();

        final boolean hasVariables = nonNull(variables);

        BufferedWriter fileWriter = null;
        if (writeToFile) {
            try {
                if (Files.notExists(file)) {
                    Files.createFile(file);
                }
                fileWriter = Files.newBufferedWriter(
                    file,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING
                );
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.err.println("Error obtaining file handle: " + ioe.getMessage());

                throw new RuntimeException("Error obtaining file handle: " + ioe.getMessage());
            }
        }

        final String input = exp.trim();

        if (verbose) {
            System.out.println("[INFO] Input string: '" + exp + "'");
            System.out.println("[INFO] Output stack: " + stack.toString());
            System.out.println("[INFO] Parsing string...");
            if (writeToFile) {
                try {
                    fileWriter.write("[INFO] Input string: '" + exp + "'" + System.lineSeparator());
                    fileWriter.write("[INFO] Output stack: " + stack.toString() + System.lineSeparator());
                    fileWriter.write("[INFO] Parsing string..." + System.lineSeparator());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    System.err.println("Error writing to file: " + ioe.getMessage());
                }
            }
        }

        long matches = VAR_PATTERN.matcher(input).results().count();
        if (!hasVariables && matches > 0) {
            System.err.println("Expression contains " + matches + " variables, but "
                    + "no variable mapping was provided");
            if (verbose) {
                System.out.println("[ERROR] Input expression contains " + matches
                        + " variables, but no variable mapping was provided");
                if (writeToFile) {
                    try {
                        fileWriter.write("[ERROR] Input expression contains " + matches
                                + " variables, but no variable mapping was provided"
                                + System.lineSeparator());
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                        System.err.println("Error writing to file: " + ioe.getMessage());
                    }
                }
            }
            throw new IllegalArgumentException("String contains " + matches
                    + " variables, but no variable mapping was provided");
        } else if (hasVariables && (int) matches != variables.size()) {
            System.err.println("Expression contains " + matches + " variables, but "
                    + variables.size() + " variables were provided");
            if (verbose) {
                System.out.println("[ERROR] Input expression contains " + matches + " variables, but "
                        + variables.size() + " variables were provided");
                if (writeToFile) {
                    try {
                        fileWriter.write("[ERROR] Input expression contains " + matches + " variables, but "
                                + variables.size() + " variables were provided"
                                + System.lineSeparator());
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                        System.err.println("Error writing to file: " + ioe.getMessage());
                    }
                }
            }
            throw new IllegalArgumentException("Expression contains " + matches + " variables, but "
                    + variables.size() + " variables were provided");
        }

        final StringBuilder token = new StringBuilder();
        int state = 0;
        Character c;
        for (int i = 0; i < input.length(); /*nop*/) {c = input.charAt(i);
            switch (state) {
                case 0:
                    if (Character.isDigit(c)) {
                        state = 1;
                    } else if (Character.isAlphabetic(c)) {
                        if (hasVariables) {
                            token.append(variables.get(String.valueOf(c)));
                            pushOperand(token);
                            ++i;
                        } else {
                            System.err.println("Variable encountered, but no variables "
                                    + "were provided");
                            throw new IllegalStateException("Variable encountered, but no "
                                    + "variables were provided");
                        }
                    } else if (INotationConverter.isOperand(c)) {
                        if (verbose) {
                            if (writeToFile)
                                logOperator(c, fileWriter);
                            else
                                logOperator(c);
                        }
                        pushOperator(c);
                        ++i;
                    } else {
                        ++i;
                    }
                    break;
                case 1:
                    if (Character.isDigit(c)) {
                        token.append(c);
                        ++i;
                    } else if (c == '.' || c == ',') {
                        token.append('.');
                        ++i;
                    } else {
                        if (verbose) {
                            if (writeToFile)
                                logOperand(token.toString(), fileWriter);
                            else
                                logOperand(token.toString());
                        }
                        pushOperand(token);
                        state = 0;
                    }
                    break;
                default:
                    throw new IllegalStateException("Illegal state: " + state);
            }
        }
        if (state > 0) {
            if (verbose) {
                if (writeToFile)
                    logOperand(token.toString(), fileWriter);
                else
                    logOperand(token.toString());
            }
            pushOperand(token);
            state = 0;
        }

        if (verbose) {
            System.out.println("[INFO] Expression parsed");
            System.out.println("[INFO] Stack: " + stack.toString());
            System.out.println("[INFO] Result: " + Double.parseDouble(stack.peek()));
            System.out.println("[INFO] Done");
            if (writeToFile) {
                try {
                    fileWriter.write("[INFO] Expression parsed" + System.lineSeparator());
                    fileWriter.write("[INFO] Stack: " + stack.toString() + System.lineSeparator());
                    fileWriter.write("[INFO] Result: " + Double.parseDouble(stack.peek())
                            + System.lineSeparator());
                    fileWriter.write("[INFO] Done" + System.lineSeparator());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    System.err.println("Error writing to file: " + ioe.getMessage());
                }
            }
        }

        if (nonNull(fileWriter)) {
            try {
                fileWriter.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.err.println("Error closing file handle: " + ioe.getMessage());
            }
        }
        
        return Double.parseDouble(stack.peek());
    }
    
    private void logOperand(String operand) {
        System.out.println("[INFO] Operand encountered: '" + operand + "'");
    }

    private void logOperand(String operand, BufferedWriter fileWriter) {
        this.logOperand(operand);
        if (nonNull(fileWriter)) {
            try {
                fileWriter.write("[INFO] Operand encountered: '" + operand + "'"
                        + System.lineSeparator());
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.err.println("Error writing to file: " + ioe.getMessage());
            }
        }
    }
    
    private void pushOperand(StringBuilder token) {
        stack.offer(requireNonNull(token).toString());
        token.delete(0, token.length());
    }

    private void pushOperator(Character operator) {
        final Operation op = Operation.of(requireNonNull(operator));
        if (isNull(op)) {
            System.err.println("Couldn't parse operator: '" + operator + "'");
            throw new IllegalArgumentException("No operation for operator '" + operator + "'");
        }
        Double secondOperand = Double.parseDouble(stack.poll());
        Double firstOperand = Double.parseDouble(stack.poll());
        stack.offer(op.operation().calculate(firstOperand, secondOperand).toString());
    }

    private void logOperator(Character operator) {
        System.out.println("[INFO] Operator encountered: '" + operator + "'");
    }

    private void logOperator(Character operator, BufferedWriter fileWriter) {
        this.logOperator(operator);
        if (nonNull(fileWriter)) {
            try {
                fileWriter.write("[INFO] Operator encountered: '" + operator + "'"
                        + System.lineSeparator());
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.err.println("Error writing to file: " + ioe.getMessage());
            }
        }
    }
    
}
