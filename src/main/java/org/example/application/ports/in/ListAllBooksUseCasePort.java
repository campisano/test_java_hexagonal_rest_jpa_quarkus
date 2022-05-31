package org.example.application.ports.in;

import java.util.List;

import org.example.application.dtos.BookDTO;

public interface ListAllBooksUseCasePort {
    List<BookDTO> execute();
}