package org.example.application.usecases;

import java.text.MessageFormat;
import java.util.Set;
import java.util.stream.Collectors;

import org.example.application.dtos.BookDTO;
import org.example.application.dtos.translators.AuthorDTOTranslator;
import org.example.application.dtos.translators.BookDTOTranslator;
import org.example.application.exceptions.AuthorInvalidException;
import org.example.application.exceptions.BookInvalidException;
import org.example.application.exceptions.IsbnAlreadyExistsException;
import org.example.application.ports.in.AddBookUseCasePort;
import org.example.application.ports.out.AuthorsRepositoryPort;
import org.example.application.ports.out.BooksRepositoryPort;
import org.example.domain.Author;
import org.example.domain.Book;

public class AddBookUseCase implements AddBookUseCasePort {

    private BooksRepositoryPort booksRepository;
    private AuthorsRepositoryPort authorsRepository;

    public AddBookUseCase(BooksRepositoryPort booksRepository, AuthorsRepositoryPort authorsRepository) {
        this.booksRepository = booksRepository;
        this.authorsRepository = authorsRepository;
    }

    @Override
    public BookDTO execute(BookDTO bookDto)
            throws IsbnAlreadyExistsException, BookInvalidException, AuthorInvalidException {

        var authors = retrieveExistingAuthors(bookDto.getAuthors());
        var book = newBook(bookDto, authors);

        return booksRepository.create(BookDTOTranslator.toDTO(book));
    }

    private Set<Author> retrieveExistingAuthors(Set<String> authorNames) throws AuthorInvalidException {
        var existingAuthors = AuthorDTOTranslator.fromDTO(authorsRepository.findByNameIn(authorNames));
        var existingAuthorNames = existingAuthors.stream().map(author -> author.getName()).collect(Collectors.toSet());
        var unExistingAuthorNames = authorNames.stream().collect(Collectors.toSet());
        unExistingAuthorNames.removeAll(existingAuthorNames);

        if (unExistingAuthorNames.size() > 0) {
            throw new AuthorInvalidException(
                    MessageFormat.format("Some of authors does not exists: {0}", unExistingAuthorNames));
        }

        return existingAuthors;
    }

    private Book newBook(BookDTO bookDto, Set<Author> authors) throws IsbnAlreadyExistsException, BookInvalidException {

        if (booksRepository.findByIsbn(bookDto.getIsbn()).isPresent()) {
            throw new IsbnAlreadyExistsException(bookDto.getIsbn());
        }

        try {
            return BookDTOTranslator.fromDTO(bookDto, authors);
        } catch (IllegalArgumentException exception) {
            throw new BookInvalidException(exception.getMessage());
        }
    }
}
