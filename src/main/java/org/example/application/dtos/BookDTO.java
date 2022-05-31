package org.example.application.dtos;

import java.util.HashSet;
import java.util.Set;

public class BookDTO {

    private String isbn;
    private String title;
    private Set<String> authors;
    private String description;

    public BookDTO(String isbn, String title, Set<String> authors, String description) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors != null ? authors : new HashSet<>();
        this.description = description;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public Set<String> getAuthors() {
        return authors;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "BookDTO [isbn=" + isbn + ", title=" + title + ", authors=" + authors + ", description=" + description
                + "]";
    }
}
