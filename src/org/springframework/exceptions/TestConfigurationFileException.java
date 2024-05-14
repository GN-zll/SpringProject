package org.springframework.exceptions;

public class TestConfigurationFileException extends Exception {
    public TestConfigurationFileException() {
        super("Error! There must be no more than one @TestConfiguration file");
    }
}
