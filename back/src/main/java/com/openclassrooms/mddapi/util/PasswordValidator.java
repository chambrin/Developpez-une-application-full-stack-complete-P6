package com.openclassrooms.mddapi.util;

import java.util.regex.Pattern;

public class PasswordValidator {
    private static final int REQUIRED_LENGTH = 8;
    private static final Pattern DIGIT_PATTERN = Pattern.compile(".*\\d.*");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile(".*[a-z].*");
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile(".*[A-Z].*");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile(".*[!@#$%^&*(),.?\":{}|<>].*");

    public static boolean isValid(String password) {
        return password != null &&
                password.length() >= REQUIRED_LENGTH &&
                DIGIT_PATTERN.matcher(password).matches() &&
                LOWERCASE_PATTERN.matcher(password).matches() &&
                UPPERCASE_PATTERN.matcher(password).matches() &&
                SPECIAL_CHAR_PATTERN.matcher(password).matches();
    }
}