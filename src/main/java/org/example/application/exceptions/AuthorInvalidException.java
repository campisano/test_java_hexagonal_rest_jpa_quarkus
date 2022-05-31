package org.example.application.exceptions;

public class AuthorInvalidException extends Exception {
    private static final long serialVersionUID = 1L;

    public AuthorInvalidException(String message) {
        super(message);
    }
}
