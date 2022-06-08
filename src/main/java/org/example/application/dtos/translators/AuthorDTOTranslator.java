package org.example.application.dtos.translators;

import java.util.HashSet;
import java.util.Set;

import org.example.application.dtos.AuthorDTO;
import org.example.domain.Author;

public class AuthorDTOTranslator {

    public static Author fromDTO(AuthorDTO dto) {
        return new Author(dto.getName());
    }

    public static Set<Author> fromDTO(Set<AuthorDTO> dtos) {
        Set<Author> authors = new HashSet<>();

        for (AuthorDTO dto : dtos) {
            authors.add(fromDTO(dto));
        }

        return authors;
    }

    public static AuthorDTO toDTO(Author model) {
        return new AuthorDTO(model.getName());
    }
}
