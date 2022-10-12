package ru.etu.operation;

public enum Operation {

    ADD('+', 1, (v1, v2) -> v1 + v2),
    SUBTRACT('-', 1, (v1, v2) -> v1 - v2),
    MULTIPLY('*', 2, (v1, v2) -> v1 * v2),
    DIVIDE('/', 2, (v1, v2) -> v1 / v2),
    SQUARE('^', 3, (v1, v2) -> Math.pow(v1, v2)),
    OPEN_PARENTHESES('(', 0, null),
    CLOSE_PARENTHESES(')', 0, null);

    private final Character symbol;

    private final Integer weight;

    private final ArithmeticOperation operation;

    private Operation(Character symbol,
                      Integer weight,
                      ArithmeticOperation operation) {
        this.symbol = symbol;
        this.weight = weight;
        this.operation = operation;
    }

    public static Operation of(Character symbol) {
        for (Operation op : Operation.values())
            if (op.symbol().equals(symbol))
                return op;
        return null;
    }

    public Character symbol() {
        return this.symbol;
    }

    public Integer weight() {
        return this.weight;
    }

    public ArithmeticOperation operation() {
        return this.operation;
    }

    @Override
    public String toString() {
        return "" + this.symbol();
    }

}
