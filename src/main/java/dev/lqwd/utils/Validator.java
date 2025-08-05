package dev.lqwd.utils;

import dev.lqwd.exception.BadRequestException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public final class Validator {

    private static final Pattern PATTERN_NAME = Pattern.compile("^(?!\\s*$)(?:[a-zA-Z ]{1,15}|[а-яА-Я ]{1,15})$");
    private static final Pattern PATTERN_PAGE = Pattern.compile("^(?!0+$)([0-9]{1,3})$");
    private static final String SUPPORTS_CHARS_MESSAGE = "'Player name' supports latina and cyrillic only, max 15 chars";
    private static final String EQUALS_NAMES_MESSAGE = "Players names should be different";
    private static final String PAGE_MESSAGE = "Incorrect page number";

    public static int parseParameter(String parameter, int defaultValue) {

        if (parameter == null || parameter.isBlank()) {
            return defaultValue;
        }

        if (PATTERN_PAGE.matcher(parameter).matches()) {
            return Integer.parseInt(parameter);
        }

        log.warn("incorrect page number: {}", parameter);
        throw new BadRequestException(PAGE_MESSAGE);

    }

    public static String parseParameter(String parameter, String defaultValue) {

        if (parameter == null || parameter.isBlank()) {
            return defaultValue;
        }

        String name = parameter.trim();

        validateName(name);
        return name;

    }

    public static void validate(String name1, String name2) throws BadRequestException {

        validateName(name1);
        validateName(name2);
        validateOnEquals(name1, name2);

    }

    private static void validateName(String name) {

        if (name == null || name.isBlank() || isIncorrectName(name)) {
            log.warn("incorrect name: {}", name);
            throw new BadRequestException(SUPPORTS_CHARS_MESSAGE);
        }

    }

    private static boolean isIncorrectName(String name) {

        return !PATTERN_NAME.matcher(name).matches();

    }

    private static void validateOnEquals(String name1, String name2) {

        if (name1.equalsIgnoreCase(name2)) {
            log.warn("Equal names: {} and {}", name1, name2);
            throw new BadRequestException(EQUALS_NAMES_MESSAGE + " " + SUPPORTS_CHARS_MESSAGE);
        }

    }

}
