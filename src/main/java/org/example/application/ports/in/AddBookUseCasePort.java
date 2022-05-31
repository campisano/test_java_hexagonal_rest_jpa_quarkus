package org.example.application.ports.in;

import org.example.application.dtos.BookDTO;
import org.example.application.exceptions.AuthorInvalidException;
import org.example.application.exceptions.BookInvalidException;
import org.example.application.exceptions.IsbnAlreadyExistsException;

public interface AddBookUseCasePort {
    BookDTO execute(BookDTO book) throws IsbnAlreadyExistsException, BookInvalidException, AuthorInvalidException;
}