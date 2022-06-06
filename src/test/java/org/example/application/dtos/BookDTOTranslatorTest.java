package org.example.application.dtos;

import java.util.Arrays;
import java.util.HashSet;

import org.example.application.dtos.translators.BookDTOTranslator;
import org.example.domain.Author;
import org.example.domain.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BookDTOTranslatorTest {

    @Test
    public void toDTO() {
        // Act
        var book = new Book("isbn", "title", new HashSet<>(Arrays.asList(new Author("author"))), "description");
        var bookDto = BookDTOTranslator.toDTO(book);

        // Assert
        Assertions.assertNotNull(bookDto);
        Assertions.assertEquals("isbn", bookDto.getIsbn());
        Assertions.assertEquals("title", bookDto.getTitle());
        Assertions.assertEquals(new HashSet<>(Arrays.asList("author")), bookDto.getAuthors());
        Assertions.assertEquals("description", bookDto.getDescription());
    }

    @Test
    public void fromDTO() {
        // Act
        var bookDto = new BookDTO("isbn", "title", new HashSet<>(Arrays.asList("author")), "description");
        var book = BookDTOTranslator.fromDTO(bookDto, new HashSet<>(Arrays.asList(new Author("author"))));

        // Assert
        Assertions.assertNotNull(book);
        Assertions.assertEquals("isbn", book.getIsbn());
        Assertions.assertEquals("title", book.getTitle());
        Assertions.assertEquals(new HashSet<>(Arrays.asList(new Author("author"))).iterator().next().getName(),
                book.getAuthors().iterator().next().getName());
        Assertions.assertEquals("description", book.getDescription());
    }
}
