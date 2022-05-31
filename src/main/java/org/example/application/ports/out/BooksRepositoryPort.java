package org.example.application.ports.out;

import java.util.List;
import java.util.Optional;

import org.example.application.dtos.BookDTO;

public interface BooksRepositoryPort {
    BookDTO create(BookDTO book);

    List<BookDTO> findAll();

    Optional<BookDTO> findByIsbn(String isbn);
}