package ru.etu.enums;

public enum Department {
    
    DAYTIME("Очная"),
    EVENING("Очно-заочная"),
    DISTANT("Заочная");

    private static final String FORM_SUFFIX = " форма обучения";

    private final String value;

    private Department(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public String getFullString() {
        return this.value.concat(FORM_SUFFIX);
    }

}
