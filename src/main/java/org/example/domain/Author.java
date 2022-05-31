package org.example.domain;

import java.text.MessageFormat;

public class Author {
    private String name;

    public Author(String name) {
        ensureCreable(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private static void ensureCreable(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException(MessageFormat.format("Name {0} is invalid", name));
        }
    }
}
