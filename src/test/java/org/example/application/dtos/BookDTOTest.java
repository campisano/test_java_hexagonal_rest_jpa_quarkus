package org.example.application.dtos;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BookDTOTest {

    @Test
    public void creation() {
        // Act
        var book = new BookDTO("isbn", "title", new HashSet<>(Arrays.asList("author")), "description");

        // Assert
        Assertions.assertEquals("isbn", book.getIsbn());
        Assertions.assertEquals("title", book.getTitle());
        Assertions.assertEquals(new HashSet<>(Arrays.asList("author")), book.getAuthors());
        Assertions.assertEquals("description", book.getDescription());
    }
}
