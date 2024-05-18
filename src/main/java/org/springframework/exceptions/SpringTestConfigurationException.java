package org.springframework.exceptions;

public class SpringTestConfigurationException extends SpringTestException {
    public SpringTestConfigurationException() {
        super("Error! There must be no more than one @TestConfiguration file");
    }
}
