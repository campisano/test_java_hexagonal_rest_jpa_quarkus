package org.example.adapters;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@QuarkusTest
public class TestHTTPAuthorsAdapter {
	@Test
	public void post() {
		String authorName = "author_name_1";
		String requestBody = String.format("{\"name\": \"%s\"}", authorName);

		Response response = rest(ContentType.JSON).body(requestBody).post("/v1/authors");

		Assertions.assertEquals(201, response.getStatusCode());
		Assertions.assertEquals(authorName, response.jsonPath().getString("name"));
	}

	@Test
	public void postAlreadyExistent() {
		rest(ContentType.JSON).body("{\"name\":\"author_name_1\"}").post("/v1/authors").then().statusCode(201);
		rest(ContentType.JSON).body("{\"name\":\"author_name_2\"}").post("/v1/authors").then().statusCode(201);

		Response response = rest(ContentType.JSON).body("{\"name\":\"author_name_1\"}").post("/v1/authors");

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
