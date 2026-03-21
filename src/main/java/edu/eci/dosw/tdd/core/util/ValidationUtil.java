package edu.eci.dosw.tdd.core.util;

public class ValidationUtil {

    private ValidationUtil() {}

    public static void validateNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void validateNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void validatePositive(int number, String message) {
        if (number <= 0) {
            throw new IllegalArgumentException(message);
        }
    }
}