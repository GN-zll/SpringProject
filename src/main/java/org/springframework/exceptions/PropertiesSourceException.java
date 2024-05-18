package org.springframework.exceptions;

public class PropertiesSourceException extends PropertyException {
    public PropertiesSourceException() {
        super("The PropertiesSource file could not be found");
    }
}

