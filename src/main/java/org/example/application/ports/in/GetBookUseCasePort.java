package org.example.application.ports.in;

import org.example.application.dtos.BookDTO;
import org.example.application.exceptions.IsbnNotExistsException;

public interface GetBookUseCasePort {
    BookDTO execute(String isbn) throws IsbnNotExistsException;
}