package org.springframework.exceptions;

public class SpringTestFileException extends SpringTestException {
    public SpringTestFileException() {
        super("Error! There must be no more than one @SpringTest file");
    }
}
