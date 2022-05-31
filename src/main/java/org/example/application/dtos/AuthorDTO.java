package org.example.application.dtos;

public class AuthorDTO {
    private String name;

    public AuthorDTO(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "AuthorDTO [name=" + name + "]";
    }
}
