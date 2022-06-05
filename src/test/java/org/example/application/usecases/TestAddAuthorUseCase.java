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
        authorsRepository.findByName_out = Optional.empty();

        addAuthorUseCase.execute(sampleAuthorDto);

        Assertions.assertEquals(sampleAuthorDto.getName(), authorsRepository.findByName_in.toString());
    }

    @Test
    void when_add_new_then_call_create() throws Exception {
        authorsRepository.findByName_out = Optional.empty();

        addAuthorUseCase.execute(sampleAuthorDto);

        Assertions.assertEquals(sampleAuthorDto.toString(), authorsRepository.create_in.toString());
    }

    @Test
    void when_add_new_then_new_returns() throws Exception {
        authorsRepository.create_out = sampleAuthorDto;
        authorsRepository.findByName_out = Optional.empty();

        AuthorDTO author = addAuthorUseCase.execute(sampleAuthorDto);

        Assertions.assertEquals(sampleAuthorDto.toString(), author.toString());
    }

    @Test
    void when_add_existent_then_throw_exception() throws Exception {
        authorsRepository.findByName_out = Optional.of(sampleAuthorDto);

        Assertions.assertThrows(AuthorAlreadyExistsException.class, () -> {
            addAuthorUseCase.execute(sampleAuthorDto);
        });
    }

    @Test
    void when_add_null_name_then_throw_exception() throws Exception {
        authorsRepository.findByName_out = Optional.empty();
        AuthorDTO invalidAuthor = new AuthorDTO(null);

        Assertions.assertThrows(AuthorInvalidException.class, () -> {
            addAuthorUseCase.execute(invalidAuthor);
        });
    }

    @Test
    void when_add_empty_name_then_throw_exception() throws Exception {
        authorsRepository.findByName_out = Optional.empty();
        AuthorDTO invalidAuthor = new AuthorDTO("");

        Assertions.assertThrows(AuthorInvalidException.class, () -> {
            addAuthorUseCase.execute(invalidAuthor);
        });
    }
}