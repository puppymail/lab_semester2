package ru.etu.notation;

import static java.util.Objects.requireNonNull;
import static ru.etu.notation.INotationConverter.weightOf;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class ReversePolishNotationConverter implements INotationConverter {

    private final Queue<String> output;

    private final Queue<Character> operands;

    public ReversePolishNotationConverter() {
        this.output = Collections.asLifoQueue(new LinkedList<>());
        this.operands = Collections.asLifoQueue(new LinkedList<>());
    }

    @Override
    public String convert(String exp) {
        operands.clear();
        output.clear();

        final String input = exp.trim();

        final StringBuilder token = new StringBuilder();
        int state = 0;
        Character c;
        for (int i = 0; i < input.length(); /*nop*/) {
            c = input.charAt(i);
            switch (state) {
                case 0:
                    if (Character.isDigit(c)) {
                        state = 1;
                    } else if (Character.isAlphabetic(c)) {
                        state = 2;
                    } else if (INotationConverter.isOperand(c)) {
                        pushOperand(c);
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
                        token.append(',');
                        ++i;
                    } else {
                        pushToken(token);
                        state = 0;
                    }
                    break;
                case 2:
                    if (Character.isAlphabetic(c)) {
                        token.append(c);
                        ++i;
                    } else {
                        pushToken(token);
                        state = 0;
                    }
                    break;
                default:
                    throw new IllegalStateException("Illegal state: " + state);
            }
        }
        if (state > 0) {
            pushToken(token);
            state = 0;
        }

        while (!operands.isEmpty()) {
            if (operands.peek() == '(' || operands.peek() == ')')
                return null;
            output.offer(String.valueOf(operands.poll()));
        }

        final StringBuilder result = new StringBuilder();
        Iterator<String> iter = output.iterator();
        while (iter.hasNext()) {
            result.insert(0, iter.next());
            if (iter.hasNext()) result.insert(0, ' ');
        }

        return result.toString();
    }

    @Override
    public Queue<String> getTokens() {
        return new LinkedList<>(this.output);
    }

    private void pushToken(StringBuilder token) {
        output.offer(requireNonNull(token).toString());
        token.delete(0, token.length());
    }

    private void pushOperand(Character operand) {
        requireNonNull(operand);
        if (operand == '(') {
            operands.offer(operand);
        } else if (operand == ')') {
            while (!operands.isEmpty()
                   && operands.peek() != '(') {
                output.offer(String.valueOf(operands.poll()));
            }
            operands.poll();
        } else {
            while (!operands.isEmpty()
                   && weightOf(operand) <= weightOf(operands.peek())) {
                output.offer(String.valueOf(operands.poll()));
            }
            operands.offer(operand);
        }
    }
    
}
