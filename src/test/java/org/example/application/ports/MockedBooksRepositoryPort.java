package org.example.application.ports;

import java.util.List;
import java.util.Optional;

import org.example.application.dtos.BookDTO;
import org.example.application.ports.out.BooksRepositoryPort;

public class MockedBooksRepositoryPort implements BooksRepositoryPort {
    public BookDTO create_in;
    public BookDTO create_out;

    public List<BookDTO> findAll_out;

    public String findByIsbn_in;
    public Optional<BookDTO> findByIsbn_out;

    @Override
    public BookDTO create(BookDTO book) {
        create_in = book;
        return create_out;
    }

    @Override
    public List<BookDTO> findAll() {
        return findAll_out;
    }

    @Override
    public Optional<BookDTO> findByIsbn(String isbn) {
        findByIsbn_in = isbn;
        return findByIsbn_out;
    }
}
