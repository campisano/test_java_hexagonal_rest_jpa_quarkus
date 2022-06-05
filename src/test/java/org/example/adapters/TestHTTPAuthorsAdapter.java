package org.example.adapters;

import java.util.Arrays;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.example.application.dtos.AuthorDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@QuarkusTest
public class TestHTTPAuthorsAdapter {

	@Test
	public void post() throws Exception {
		AuthorDTO author = new AuthorDTO("author1");

		Response response = rest(ContentType.JSON).body(author).post("/v1/authors");

		Assertions.assertEquals(201, response.getStatusCode());
		ObjectMapper om = new ObjectMapper();
		Assertions.assertEquals(om.readTree(om.writeValueAsString(author)),
				om.readTree(response.jsonPath().prettify()));
	}

	@Test
	public void postAlreadyExistent() {
		AuthorDTO a1 = new AuthorDTO("author1");
		AuthorDTO a2 = new AuthorDTO("author2");
		Arrays.asList(a1, a2).forEach(a -> {
			rest(ContentType.JSON).body(a).post("/v1/authors");
		});

		AuthorDTO author = new AuthorDTO("author1");
		Response response = rest(ContentType.JSON).body(author).post("/v1/authors");

		Assertions.assertEquals(422, response.getStatusCode());
		Assertions.assertEquals("", response.getBody().asString());
	}

	private RequestSpecification rest(ContentType type) {
		return RestAssured.given().contentType(type);
	}

	@Inject
	EntityManager entityManager;

	@AfterEach
	@Transactional
	public void afterEach() {
		entityManager.createNativeQuery("DELETE FROM author").executeUpdate();
	}
}
