package org.example.adapters;

import java.util.Arrays;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.example.TestUtils;
import org.example.application.dtos.AuthorDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class TestHTTPAuthorsAdapter {

    @Test
    public void post() throws Exception {
        // Act
        var author = new AuthorDTO("author1");
        var response = TestUtils.JsonRequest().body(author).post("/v1/authors");

        // Assert
        Assertions.assertEquals(201, response.getStatusCode());
        Assertions.assertEquals(TestUtils.toJson(author), TestUtils.toJson(response.jsonPath().prettify()));
    }

    @Test
    public void postAlreadyExistent() {
        // Arrange
        var a1 = new AuthorDTO("author1");
        var a2 = new AuthorDTO("author2");
        Arrays.asList(a1, a2).forEach(a -> {
            TestUtils.JsonRequest().body(a).post("/v1/authors");
        });

        // Act
        var author = new AuthorDTO("author1");
        var response = TestUtils.JsonRequest().body(author).post("/v1/authors");

        // Assert
        Assertions.assertEquals(422, response.getStatusCode());
        Assertions.assertEquals("", response.getBody().asString());
    }

    @Inject
    EntityManager entityManager;

    @AfterEach
    @Transactional
    public void afterEach() {
        entityManager.createNativeQuery("DELETE FROM author").executeUpdate();
    }
}
