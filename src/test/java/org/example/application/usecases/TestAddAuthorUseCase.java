package org.example.application.usecases;

import java.util.Optional;

import org.example.application.dtos.AuthorDTO;
import org.example.application.exceptions.AuthorAlreadyExistsException;
import org.example.application.exceptions.AuthorInvalidException;
import org.example.application.ports.MockedAuthorsRepositoryPort;
import org.example.application.ports.in.AddAuthorUseCasePort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestAddAuthorUseCase {

    static final AuthorDTO sampleAuthorDto = new AuthorDTO("sample_name");

    MockedAuthorsRepositoryPort authorsRepository;
    AddAuthorUseCasePort addAuthorUseCase;

    @BeforeEach
    void beforeEach() {
        authorsRepository = new MockedAuthorsRepositoryPort();
        addAuthorUseCase = new AddAuthorUseCase(authorsRepository);
    }

    @Test
    void when_add_new_then_call_findByName() throws Exception {
        // Arrange
        authorsRepository.findByName_out = Optional.empty();

        // Act
        addAuthorUseCase.execute(sampleAuthorDto);

        // Assert
        Assertions.assertEquals(sampleAuthorDto.getName(), authorsRepository.findByName_in.toString());
    }

    @Test
    void when_add_new_then_call_create() throws Exception {
        // Arrange
        authorsRepository.findByName_out = Optional.empty();

        // Act
        addAuthorUseCase.execute(sampleAuthorDto);

        // Assert
        Assertions.assertEquals(sampleAuthorDto.toString(), authorsRepository.create_in.toString());
    }

    @Test
    void when_add_new_then_new_returns() throws Exception {
        // Arrange
        authorsRepository.create_out = sampleAuthorDto;
        authorsRepository.findByName_out = Optional.empty();

        // Act
        var author = addAuthorUseCase.execute(sampleAuthorDto);

        // Assert
        Assertions.assertEquals(sampleAuthorDto.toString(), author.toString());
    }

    @Test
    void when_add_existent_then_throw_exception() throws Exception {
        // Arrange
        authorsRepository.findByName_out = Optional.of(sampleAuthorDto);

        // Act
        var exception = Assertions.assertThrows(Exception.class, () -> {
            addAuthorUseCase.execute(sampleAuthorDto);
        });

        // Assert
        Assertions.assertEquals(AuthorAlreadyExistsException.class, exception.getClass());
        Assertions.assertTrue(exception.getMessage().matches("Author(.*)already exists"));
    }

    @Test
    void when_add_null_name_then_throw_exception() throws Exception {
        // Arrange
        authorsRepository.findByName_out = Optional.empty();

        // Act
        var invalidAuthor = new AuthorDTO(null);
        var exception = Assertions.assertThrows(Exception.class, () -> {
            addAuthorUseCase.execute(invalidAuthor);
        });

        // Assert
        Assertions.assertEquals(AuthorInvalidException.class, exception.getClass());
        Assertions.assertTrue(exception.getMessage().matches("Name(.*)is invalid"));
    }

    @Test
    void when_add_empty_name_then_throw_exception() throws Exception {
        // Arrange
        authorsRepository.findByName_out = Optional.empty();

        // Act
        var invalidAuthor = new AuthorDTO("");
        var exception = Assertions.assertThrows(Exception.class, () -> {
            addAuthorUseCase.execute(invalidAuthor);
        });

        // Assert
        Assertions.assertEquals(AuthorInvalidException.class, exception.getClass());
        Assertions.assertTrue(exception.getMessage().matches("Name(.*)is invalid"));
    }
}