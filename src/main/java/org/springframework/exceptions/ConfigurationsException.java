package org.springframework.exceptions;

public class ConfigurationsException extends Exception {
    public ConfigurationsException() {
        super("Error! There must be no more than one @Configuration file");
    }
}
