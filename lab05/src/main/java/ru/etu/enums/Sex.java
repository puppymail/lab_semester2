package ru.etu.enums;

public enum Sex {
    
    MALE("Male"),
    FEMALE("Female");

    private final String value;
    private final Character chara;

    public static Sex of(final Character character) {
        for (Sex s : Sex.values()) {
            if (s.chara == Character.toUpperCase(character)) {
                return s;
            }
        }

        return null;
    }

    private Sex(final String value) {
        this.value = value;
        this.chara = value.charAt(0);
    }

    public String getValue() {
        return this.value;
    }

    public Character getAsCharacter() {
        return this.chara;
    }

}
