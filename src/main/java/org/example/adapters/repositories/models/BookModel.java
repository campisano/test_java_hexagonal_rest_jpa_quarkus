package org.example.adapters.repositories.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "book")
public class BookModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<AuthorModel> authors = new HashSet<>();

    private String description;

    BookModel() {
    }

    public BookModel(String isbn, String title, Set<AuthorModel> authors, String description) {
        this.isbn = isbn;
        this.title = title;
        this.setAuthor(authors);
        this.description = description;
    }

    public long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<AuthorModel> getAuthors() {
        return authors;
    }

    public void setAuthor(Set<AuthorModel> authors) {
        this.authors = authors != null ? authors : new HashSet<>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
