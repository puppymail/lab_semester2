package ru.etu.notation;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static ru.etu.notation.INotationConverter.weightOf;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class ReversePolishNotationConverter implements INotationConverter {

    private final Queue<String> output;

    private final Queue<Character> operators;

    private Path file = Paths.get(DEFAULT_FILE_NAME);

    public ReversePolishNotationConverter() {
        this.output = Collections.asLifoQueue(new LinkedList<>());
        this.operators = Collections.asLifoQueue(new LinkedList<>());
    }

    public ReversePolishNotationConverter(String filePath) {
        this();
        this.file = Paths.get(filePath);
    }

    @Override
    public String convert(String exp, final boolean verbose, final boolean writeToFile) {
        operators.clear();
        output.clear();
        
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
            System.out.println("[INFO] Input expression: '" + exp + "'");
            System.out.println("[INFO] Output stack: " + output.toString());
            System.out.println("[INFO] Operators stack: " + operators.toString());
            System.out.println("[INFO] Parsing expression...");
            if (writeToFile) {
                try {
                    fileWriter.write("[INFO] Input expression: '" + exp + "'" + System.lineSeparator());
                    fileWriter.write("[INFO] Output stack: " + output.toString() + System.lineSeparator());
                    fileWriter.write("[INFO] Operators stack: " + operators.toString() + System.lineSeparator());
                    fileWriter.write("[INFO] Parsing expression..." + System.lineSeparator());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    System.err.println("Error writing to file: " + ioe.getMessage());
                }
            }
        }

        final StringBuilder token = new StringBuilder();
        int state = 0;
        boolean hasLastPushedOperator = true;
        Character c;
        for (int i = 0; i < input.length(); /*nop*/) {
            c = input.charAt(i);
            switch (state) {
                case 0:
                    if (Character.isDigit(c)) {
                        if (!hasLastPushedOperator) {
                            if (verbose) {
                                System.out.println("[ERROR] Illegal expression - 2 operands "
                                        + "cannot follow each other: " + input);
                                if (writeToFile) {
                                    try {
                                        fileWriter.write("[ERROR] Illegal expression - 2 operands "
                                                + "cannot follow each other: " + input + System.lineSeparator());
                                    } catch (IOException ioe) {
                                        ioe.printStackTrace();
                                        System.err.println("Error writing to file: " + ioe.getMessage());
                                    }
                                }
                            }
                            throw new IllegalArgumentException("Illegal expression - 2 operands "
                                    + "cannot follow each other: " + input);
                        }
                        hasLastPushedOperator = false;
                        state = 1;
                    } else if (Character.isAlphabetic(c)) {
                        if (!hasLastPushedOperator) {
                            if (verbose) {
                                System.out.println("[ERROR] Illegal expression - 2 operands "
                                        + "cannot follow each other: " + input);
                                if (writeToFile) {
                                    try {
                                        fileWriter.write("[ERROR] Illegal expression - 2 operands "
                                                + "cannot follow each other: " + input + System.lineSeparator());
                                    } catch (IOException ioe) {
                                        ioe.printStackTrace();
                                        System.err.println("Error writing to file: " + ioe.getMessage());
                                    }
                                }
                            }
                            throw new IllegalArgumentException("Illegal expression - 2 operands "
                                    + "cannot follow each other: " + input);
                        }
                        hasLastPushedOperator = false;
                        token.append(c);
                        if (verbose) {
                            if (writeToFile)
                                logOperand(token.toString(), fileWriter);
                            else
                                logOperand(token.toString());
                        }
                        pushOperand(token);
                        ++i;
                    } else if (INotationConverter.isOperand(c)) {
                        hasLastPushedOperator = true;
                        if (verbose) {
                            if (writeToFile)
                                logOperator(c, fileWriter);
                            else
                                logOperator(c);
                        }
                        pushOperator(c);
                        ++i;
                    } else if (Character.isSpaceChar(c)) {
                        ++i;
                    } else {
                        System.err.println("Illegal character at position " + i + ": '" + c + "'");
                        if (verbose) {
                            System.out.println("[ERROR] Illegal character at position " + i
                                    + ": '" + c + "'");
                            if (writeToFile) {
                                try {
                                    fileWriter.write("[ERROR] Illegal character at position " + i
                                            + ": '" + c + "'" + System.lineSeparator());
                                } catch (IOException ioe) {
                                    ioe.printStackTrace();
                                    System.err.println("Error writing to file: " + ioe.getMessage());
                                }
                            }
                        }
                        throw new IllegalArgumentException("Illegal character at position " + i + ": '" + c + "'");
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
            System.out.println("[INFO] Output stack: " + output.toString());
            System.out.println("[INFO] Operators stack: " + operators.toString());
            System.out.println("[INFO] Moving operands to output stack");
            if (writeToFile) {
                try {
                    fileWriter.write("[INFO] Expression parsed" + System.lineSeparator());
                    fileWriter.write("[INFO] Output stack: " + output.toString() + System.lineSeparator());
                    fileWriter.write("[INFO] Operators stack: " + operators.toString() + System.lineSeparator());
                    fileWriter.write("[INFO] Moving operations to output stack" + System.lineSeparator());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    System.err.println("Error writing to file: " + ioe.getMessage());
                }
            }
        }

        while (!operators.isEmpty()) {
            if (operators.peek() == '(' || operators.peek() == ')') {
                if (verbose) {
                    System.out.println("[ERROR] Illegal expression - uneven amount of "
                            + "parentheses: " + input);
                    if (writeToFile) {
                        try {
                            fileWriter.write("[ERROR] Illegal expression - uneven amount of "
                                    + "parentheses: " + input + System.lineSeparator());
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                            System.err.println("Error writing to file: " + ioe.getMessage());
                        }
                    }
                }
                throw new IllegalArgumentException("Illegal expression - uneven amount of "
                        + "parentheses: " + input);
            }
            output.offer(String.valueOf(operators.poll()));
        }

        final StringBuilder result = new StringBuilder();
        Iterator<String> iter = output.iterator();
        while (iter.hasNext()) {
            result.insert(0, iter.next());
            if (iter.hasNext()) result.insert(0, ' ');
        }
        
        if (verbose) {
            System.out.println("[INFO] Finished moving operands");
            System.out.println("[INFO] Output stack: " + output.toString());
            System.out.println("[INFO] Operators stack: " + operators.toString());
            System.out.println("[INFO] Reverse polish notation expression: " + result.toString());
            System.out.println("[INFO] Done");
            if (writeToFile) {
                try {
                    fileWriter.write("[INFO] Finished moving operands" + System.lineSeparator());
                    fileWriter.write("[INFO] Output stack: " + output.toString()
                            + System.lineSeparator());
                    fileWriter.write("[INFO] Operators stack: " + operators.toString()
                            + System.lineSeparator());
                    fileWriter.write("[INFO] Reverse polish notation expression: "
                            + result.toString() + System.lineSeparator());
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

        return result.toString();
    }

    @Override
    public Queue<String> getTokens() {
        return new LinkedList<>(this.output);
    }

    private void pushOperand(StringBuilder token) {
        output.offer(requireNonNull(token).toString());
        token.delete(0, token.length());
    }

    private void pushOperator(Character operator) {
        requireNonNull(operator);
        if (operator == '(') {
            operators.offer(operator);
        } else if (operator == ')') {
            while (!operators.isEmpty()
                   && operators.peek() != '(') {
                output.offer(String.valueOf(operators.poll()));
            }
            operators.poll();
        } else {
            while (!operators.isEmpty()
                   && weightOf(operator) <= weightOf(operators.peek())) {
                output.offer(String.valueOf(operators.poll()));
            }
            operators.offer(operator);
        }
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
