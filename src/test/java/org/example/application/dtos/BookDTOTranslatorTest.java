package org.example.application.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.HashSet;

import org.example.application.dtos.translators.BookDTOTranslator;
import org.example.domain.Author;
import org.example.domain.Book;
import org.junit.jupiter.api.Test;

public class BookDTOTranslatorTest {

    @Test
    public void toDTO() {
        Book book = new Book("isbn", "title", new HashSet<>(Arrays.asList(new Author("author"))), "description");

        BookDTO bookDto = BookDTOTranslator.toDTO(book);

        assertNotNull(bookDto);
        assertEquals("isbn", bookDto.getIsbn());
        assertEquals("title", bookDto.getTitle());
        assertEquals(new HashSet<>(Arrays.asList("author")), bookDto.getAuthors());
        assertEquals("description", bookDto.getDescription());
    }

    @Test
    public void fromDTO() {
        BookDTO bookDto = new BookDTO("isbn", "title", new HashSet<>(Arrays.asList("author")), "description");

        Book book = BookDTOTranslator.fromDTO(bookDto, new HashSet<>(Arrays.asList(new Author("author"))));

        assertNotNull(book);
        assertEquals("isbn", book.getIsbn());
        assertEquals("title", book.getTitle());
        assertEquals(new HashSet<>(Arrays.asList(new Author("author"))).iterator().next().getName(),
                book.getAuthors().iterator().next().getName());
        assertEquals("description", book.getDescription());
    }
}
