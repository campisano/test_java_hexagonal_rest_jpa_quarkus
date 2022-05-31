package org.example.adapters.repositories.models.translators;

import java.util.Set;
import java.util.stream.Collectors;

import org.example.adapters.repositories.models.AuthorModel;
import org.example.adapters.repositories.models.BookModel;
import org.example.application.dtos.BookDTO;

public class BookModelTranslator {

    public static BookModel fromDTO(BookDTO dto, Set<AuthorModel> authors) {
        return new BookModel(dto.getIsbn(), dto.getTitle(), authors, dto.getDescription());
    }

    public static BookDTO toDTO(BookModel model) {
        Set<String> authorNames = model.getAuthors().stream().map(author -> author.getName())
                .collect(Collectors.toSet());
        return new BookDTO(model.getIsbn(), model.getTitle(), authorNames, model.getDescription());
    }
}
