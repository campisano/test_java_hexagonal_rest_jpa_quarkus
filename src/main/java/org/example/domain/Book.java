package org.example.domain;

import java.text.MessageFormat;
import java.util.Set;

public class Book {
    private String isbn;
    private String title;
    private Set<Author> authors;
    private String description;

    public Book(String isbn, String title, Set<Author> authors, String description) {
        ensureValidForCreation(isbn, title, authors, description);
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.description = description;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public String getDescription() {
        return description;
    }

    private static void ensureValidForCreation(String isbn, String title, Set<Author> authors, String description) {
        if (isbn == null || isbn.length() == 0) {
            throw new IllegalArgumentException(MessageFormat.format("Isbn {0} is invalid", isbn));
        }

        if (title == null || title.length() == 0) {
            throw new IllegalArgumentException(MessageFormat.format("Title {0} is invalid", title));
        }

        if (authors == null || authors.size() == 0) {
            throw new IllegalArgumentException(MessageFormat
                    .format("Book authors are invalid, expected 1 or more authors, requested {0}", authors));
        }
    }
}
