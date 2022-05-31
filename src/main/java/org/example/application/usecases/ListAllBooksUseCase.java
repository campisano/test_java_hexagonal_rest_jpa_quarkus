package org.example.application.usecases;

import java.util.List;

import org.example.application.dtos.BookDTO;
import org.example.application.ports.in.ListAllBooksUseCasePort;
import org.example.application.ports.out.BooksRepositoryPort;

public class ListAllBooksUseCase implements ListAllBooksUseCasePort {

    private BooksRepositoryPort booksRepository;

    public ListAllBooksUseCase(BooksRepositoryPort booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public List<BookDTO> execute() {
        return booksRepository.findAll();
    }
}
