package org.example.application.dtos.translators;

import java.util.Set;
import java.util.stream.Collectors;

import org.example.application.dtos.BookDTO;
import org.example.domain.Author;
import org.example.domain.Book;

public class BookDTOTranslator {

    public static Book fromDTO(BookDTO dto, Set<Author> authors) {
        return new Book(dto.getIsbn(), dto.getTitle(), authors, dto.getDescription());
    }

    public static BookDTO toDTO(Book model) {
        var authorNames = model.getAuthors().stream().map(author -> author.getName()).collect(Collectors.toSet());

        return new BookDTO(model.getIsbn(), model.getTitle(), authorNames, model.getDescription());
    }
}
