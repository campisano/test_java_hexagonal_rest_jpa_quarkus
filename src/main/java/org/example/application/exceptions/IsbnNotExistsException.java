package org.example.application.exceptions;

import java.text.MessageFormat;

public class IsbnNotExistsException extends Exception {
    private static final long serialVersionUID = 1L;

    public IsbnNotExistsException(String isbn) {
        super(MessageFormat.format("Book with isbn {0} not exists", isbn));
    }
}
