package org.example.application.usecases;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.example.application.dtos.BookDTO;
import org.example.application.exceptions.IsbnNotExistsException;
import org.example.application.ports.MockedBooksRepositoryPort;
import org.example.application.ports.in.GetBookUseCasePort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestGetBookUseCase {

    static final String sampleIsbn = "sample_isbn";
    static final String sampleAuthorName = "sample_author";
    static final BookDTO sampleBookDto = new BookDTO(sampleIsbn, "sample_title",
            new HashSet<>(Arrays.asList(sampleAuthorName)), "sample_description");

    MockedBooksRepositoryPort booksRepository;
    GetBookUseCasePort getBookUseCase;

    @BeforeEach
    void beforeEach() {
        booksRepository = new MockedBooksRepositoryPort();
        getBookUseCase = new GetBookUseCase(booksRepository);
    }

    @Test
    void when_get_existent_then_call_findByIsbn() throws Exception {
        // Arrange
        booksRepository.findByIsbn_out = Optional.of(sampleBookDto);

        // Act
        getBookUseCase.execute(sampleIsbn);

        // Assert
        Assertions.assertEquals(sampleBookDto.getIsbn(), booksRepository.findByIsbn_in.toString());
    }

    @Test
    void when_get_existent_then_it_returns() throws Exception {
        // Arrange
        booksRepository.findByIsbn_out = Optional.of(sampleBookDto);

        // Act
        var book = getBookUseCase.execute(sampleIsbn);

        // Assert
        Assertions.assertEquals(sampleBookDto.toString(), book.toString());
    }

    @Test
    void when_get_not_existent_then_throw_exception() throws Exception {
        // Arrange
        booksRepository.findByIsbn_out = Optional.empty();

        // Act
        var exception = Assertions.assertThrows(Exception.class, () -> {
            getBookUseCase.execute(sampleIsbn);
        });

        // Assert
        Assertions.assertEquals(IsbnNotExistsException.class, exception.getClass());
        Assertions.assertTrue(exception.getMessage().matches("Book(.*)not exists"));
    }
}