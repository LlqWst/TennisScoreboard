package dev.lqwd.utils;

import dev.lqwd.exception.BadRequestException;

import java.util.regex.Pattern;

public final class Validator {

    private Validator() {
    }

    private static final Pattern PATTERN_NAME = Pattern.compile("^(?!\\s*$)[a-zA-Z ]{1,15}$");
    private static final String SUPPORTS_CHARS_MESSAGE = "'Player name' supports latina only, max 15 chars";
    private static final String MISSING_NAME_MESSAGE = "Please provide name for %s";
    private static final String EQUALS_NAMES_MESSAGE = "Players names should be different";

    public static void validate(String name1, String name2) throws BadRequestException {

        validateName(name1, "Player1");
        validateName(name2, "Player2");
        validateOnEquals(name1, name2);

    }

    private static void validateName(String name, String player) {

        if (name.isBlank()) {
            throw new BadRequestException(String.format(MISSING_NAME_MESSAGE + ", " + SUPPORTS_CHARS_MESSAGE, player));
        } else if (isIncorrectName(name)) {
            throw new BadRequestException(SUPPORTS_CHARS_MESSAGE);
        }

    }

    private static boolean isIncorrectName(String name) {

        return !PATTERN_NAME.matcher(name).matches();

    }

    private static void validateOnEquals(String name1, String name2) {

        if (name1.equalsIgnoreCase(name2)) {
            throw new BadRequestException(EQUALS_NAMES_MESSAGE);
        }

    }

}
