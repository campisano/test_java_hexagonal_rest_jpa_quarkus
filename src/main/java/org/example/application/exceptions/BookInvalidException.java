package org.example.application.exceptions;

public class BookInvalidException extends Exception {
    private static final long serialVersionUID = 1L;

    public BookInvalidException(String message) {
        super(message);
    }
}
