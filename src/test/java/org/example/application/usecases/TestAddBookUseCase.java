package org.example.application.usecases;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.example.application.dtos.AuthorDTO;
import org.example.application.dtos.BookDTO;
import org.example.application.exceptions.AuthorInvalidException;
import org.example.application.exceptions.BookInvalidException;
import org.example.application.exceptions.IsbnAlreadyExistsException;
import org.example.application.ports.MockedAuthorsRepositoryPort;
import org.example.application.ports.MockedBooksRepositoryPort;
import org.example.application.ports.in.AddBookUseCasePort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestAddBookUseCase {

    static final String sampleAuthorName = "sample_author";
    static final BookDTO sampleBookDto = new BookDTO("sample_isbn", "sample_title",
            new HashSet<>(Arrays.asList(sampleAuthorName)), "sample_description");

    MockedBooksRepositoryPort booksRepository;
    MockedAuthorsRepositoryPort authorsRepository;
    AddBookUseCasePort addBookUseCase;

    @BeforeEach
    void beforeEach() {
        authorsRepository = new MockedAuthorsRepositoryPort();
        booksRepository = new MockedBooksRepositoryPort();
        addBookUseCase = new AddBookUseCase(booksRepository, authorsRepository);
    }

    @Test
    void when_add_new_then_call_findByIsbn() throws Exception {
        authorsRepository.findByNameIn_out = new HashSet<>(Arrays.asList(new AuthorDTO(sampleAuthorName)));
        booksRepository.findByIsbn_out = Optional.empty();

        addBookUseCase.execute(sampleBookDto);

        Assertions.assertEquals(sampleBookDto.getIsbn(), booksRepository.findByIsbn_in.toString());
    }

    @Test
    void when_add_new_then_call_create() throws Exception {
        authorsRepository.findByNameIn_out = new HashSet<>(Arrays.asList(new AuthorDTO(sampleAuthorName)));
        booksRepository.findByIsbn_out = Optional.empty();

        addBookUseCase.execute(sampleBookDto);

        Assertions.assertEquals(sampleBookDto.toString(), booksRepository.create_in.toString());
    }

    @Test
    void when_add_new_then_new_returns() throws Exception {
        authorsRepository.findByNameIn_out = new HashSet<>(Arrays.asList(new AuthorDTO(sampleAuthorName)));
        booksRepository.create_out = sampleBookDto;
        booksRepository.findByIsbn_out = Optional.empty();

        BookDTO book = addBookUseCase.execute(sampleBookDto);

        Assertions.assertEquals(sampleBookDto.toString(), book.toString());
    }

    @Test
    void when_add_existent_then_throw_exception() throws Exception {
        authorsRepository.findByNameIn_out = new HashSet<>(Arrays.asList(new AuthorDTO(sampleAuthorName)));
        booksRepository.findByIsbn_out = Optional.of(sampleBookDto);

        Assertions.assertThrows(IsbnAlreadyExistsException.class, () -> {
            addBookUseCase.execute(sampleBookDto);
        });
    }

    @Test
    void when_add_null_isbn_then_throw_exception() throws Exception {
        authorsRepository.findByNameIn_out = new HashSet<>(Arrays.asList(new AuthorDTO(sampleAuthorName)));
        booksRepository.findByIsbn_out = Optional.empty();
        BookDTO invalidBook = new BookDTO(null, "title", new HashSet<>(Arrays.asList(sampleAuthorName)), "desc");

        Assertions.assertThrows(BookInvalidException.class, () -> {
            addBookUseCase.execute(invalidBook);
        });
    }

    @Test
    void when_add_empty_isbn_then_throw_exception() throws Exception {
        authorsRepository.findByNameIn_out = new HashSet<>(Arrays.asList(new AuthorDTO(sampleAuthorName)));
        booksRepository.findByIsbn_out = Optional.empty();
        BookDTO invalidBook = new BookDTO("", "title", new HashSet<>(Arrays.asList(sampleAuthorName)), "desc");

        Assertions.assertThrows(BookInvalidException.class, () -> {
            addBookUseCase.execute(invalidBook);
        });
    }

    @Test
    void when_add_invalid_title_then_throw_exception() throws Exception {
        authorsRepository.findByNameIn_out = new HashSet<>(Arrays.asList(new AuthorDTO(sampleAuthorName)));
        booksRepository.findByIsbn_out = Optional.empty();
        BookDTO invalidBook = new BookDTO("isbn", null, new HashSet<>(Arrays.asList(sampleAuthorName)), "desc");

        Assertions.assertThrows(BookInvalidException.class, () -> {
            addBookUseCase.execute(invalidBook);
        });
    }

    @Test
    void when_add_invalid_author_then_throw_exception() throws Exception {
        authorsRepository.findByNameIn_out = new HashSet<>();
        booksRepository.findByIsbn_out = Optional.empty();
        BookDTO invalidBook = new BookDTO("isbn", "title", null, "desc");

        Assertions.assertThrows(BookInvalidException.class, () -> {
            addBookUseCase.execute(invalidBook);
        });
    }

    @Test
    void when_add_unexistent_author_then_throw_exception() throws Exception {
        authorsRepository.findByNameIn_out = new HashSet<>();
        booksRepository.findByIsbn_out = Optional.empty();
        BookDTO invalidBook = new BookDTO("isbn", "title", new HashSet<>(Arrays.asList(sampleAuthorName)), "desc");

        Assertions.assertThrows(AuthorInvalidException.class, () -> {
            addBookUseCase.execute(invalidBook);
        });
    }
}