package org.springframework.exceptions;

public class PropertyNotFoundException extends PropertyException {
    public PropertyNotFoundException() {
        super("Property not found in Property Source file");
    }
}
