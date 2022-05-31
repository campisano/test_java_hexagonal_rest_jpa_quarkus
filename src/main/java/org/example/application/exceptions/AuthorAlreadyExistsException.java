package org.example.application.exceptions;

import java.text.MessageFormat;

public class AuthorAlreadyExistsException extends Exception {
    private static final long serialVersionUID = 1L;

    public AuthorAlreadyExistsException(String name) {
        super(MessageFormat.format("Author with name {0} already exists", name));
    }
}
