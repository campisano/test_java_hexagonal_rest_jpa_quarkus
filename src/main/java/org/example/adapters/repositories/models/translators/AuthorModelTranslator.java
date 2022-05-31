package org.example.adapters.repositories.models.translators;

import java.util.HashSet;
import java.util.Set;

import org.example.adapters.repositories.models.AuthorModel;
import org.example.application.dtos.AuthorDTO;

public class AuthorModelTranslator {

    public static AuthorModel fromDTO(AuthorDTO dto) {
        return new AuthorModel(dto.getName());
    }

    public static AuthorDTO toDTO(AuthorModel model) {
        return new AuthorDTO(model.getName());
    }

    public static Set<AuthorDTO> toDTO(Set<AuthorModel> models) {
        Set<AuthorDTO> dtos = new HashSet<>();
        for (AuthorModel model : models) {
            dtos.add(toDTO(model));
        }
        return dtos;
    }
}
