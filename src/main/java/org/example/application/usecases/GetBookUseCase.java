package org.example.application.usecases;

import java.util.Optional;

import org.example.application.dtos.BookDTO;
import org.example.application.exceptions.IsbnNotExistsException;
import org.example.application.ports.in.GetBookUseCasePort;
import org.example.application.ports.out.BooksRepositoryPort;

public class GetBookUseCase implements GetBookUseCasePort {

    private BooksRepositoryPort booksRepository;

    public GetBookUseCase(BooksRepositoryPort booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public BookDTO execute(String isbn) throws IsbnNotExistsException {
        Optional<BookDTO> book = booksRepository.findByIsbn(isbn);

        if (!book.isPresent()) {
            throw new IsbnNotExistsException(isbn);
        }

        return book.get();
    }
}
