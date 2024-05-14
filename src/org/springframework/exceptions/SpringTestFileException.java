package org.springframework.exceptions;

public class SpringTestFileException extends Exception{
    public SpringTestFileException() {
        super("Error! There must be one @SpringTest file");
    }
}
