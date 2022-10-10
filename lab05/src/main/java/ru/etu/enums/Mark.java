package ru.etu.enums;

public enum Mark {

    _1(1, "плохо"),
    _2(2, "неудовлетворительно"),
    _3(3, "удовлетворительно"),
    _4(4, "хорошо"),
    _5(5, "отлично");

    private final String textValue;
    private final Integer intValue;

    public static Mark of(final Integer mark) {
        for (Mark m : Mark.values()) {
            if (m.intValue == mark) {
                return m;
            }
        }

        return null;
    }

    private Mark(final Integer intValue, final String textValue) {
        this.intValue = intValue;
        this.textValue = textValue;
    }

    public String getTextValue() {
        return this.textValue;
    }

    public Integer getIntValue() {
        return this.intValue;
    }

    public Boolean isValid() {
        return this.intValue > 2;
    }

    @Override
    public String toString() {
        return Integer.toString(this.getIntValue());
    }
    
}
