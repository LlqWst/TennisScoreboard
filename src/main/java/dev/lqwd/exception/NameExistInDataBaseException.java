package dev.lqwd.exception;

public class NameExistInDataBaseException extends RuntimeException {
    public NameExistInDataBaseException(String message) {
        super(message);
    }
}
