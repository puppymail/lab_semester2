package ru.etu.common;

import static java.util.Objects.nonNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityUtils {
    
    public static <T> void setField(final Consumer<T> setter, final Supplier<T> getter) {
        T value = getter.get();

        if (nonNull(value)) {
            if (value instanceof String) {
                if (!((String) value).isBlank()) {
                    setter.accept(value);
                    return;
                }
            }
            setter.accept(value);
        }
    }

}
