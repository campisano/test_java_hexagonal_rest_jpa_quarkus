package org.example.application.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class BookDTOTest {

    @Test
    public void creation() {
        BookDTO book = new BookDTO("isbn", "title", new HashSet<>(Arrays.asList("author")), "description");

        assertEquals("isbn", book.getIsbn());
        assertEquals("title", book.getTitle());
        assertEquals(new HashSet<>(Arrays.asList("author")), book.getAuthors());
        assertEquals("description", book.getDescription());
    }
}
