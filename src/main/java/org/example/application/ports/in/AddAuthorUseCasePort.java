package org.example.application.ports.in;

import org.example.application.dtos.AuthorDTO;
import org.example.application.exceptions.AuthorAlreadyExistsException;
import org.example.application.exceptions.AuthorInvalidException;

public interface AddAuthorUseCasePort {
    public AuthorDTO execute(AuthorDTO author) throws AuthorInvalidException, AuthorAlreadyExistsException;
}
