package org.springframework.exceptions;

public class IncorrectClassPropertyException extends PropertyException {
    public IncorrectClassPropertyException() {
        super("Incorrect property class");
    }
}
