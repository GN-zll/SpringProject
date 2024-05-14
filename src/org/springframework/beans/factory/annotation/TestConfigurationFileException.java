package org.springframework.beans.factory.annotation;

public class TestConfigurationFileException extends Exception {
    public TestConfigurationFileException() {
        super("Error! There must be one @TestConfiguration file");
    }
}
