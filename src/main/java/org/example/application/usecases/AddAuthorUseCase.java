package org.example.application.usecases;

import org.example.application.dtos.AuthorDTO;
import org.example.application.dtos.translators.AuthorDTOTranslator;
import org.example.application.exceptions.AuthorAlreadyExistsException;
import org.example.application.exceptions.AuthorInvalidException;
import org.example.application.ports.in.AddAuthorUseCasePort;
import org.example.application.ports.out.AuthorsRepositoryPort;
import org.example.domain.Author;

public class AddAuthorUseCase implements AddAuthorUseCasePort {

    private AuthorsRepositoryPort authorsRepository;

    public AddAuthorUseCase(AuthorsRepositoryPort authorsRepository) {
        this.authorsRepository = authorsRepository;
    }

    @Override
    public AuthorDTO execute(AuthorDTO authorDto) throws AuthorInvalidException, AuthorAlreadyExistsException {
        Author author;

        try {
            author = AuthorDTOTranslator.fromDTO(authorDto);
        } catch (IllegalArgumentException exception) {
            throw new AuthorInvalidException(exception.getMessage());
        }

        if (authorsRepository.findByName(author.getName()).isPresent()) {
            throw new AuthorAlreadyExistsException(author.getName());
        }

        return authorsRepository.create(AuthorDTOTranslator.toDTO(author));
    }
}
