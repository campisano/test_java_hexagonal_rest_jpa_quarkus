package org.example.adapters;

import java.util.Arrays;
import java.util.HashSet;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.example.application.dtos.AuthorDTO;
import org.example.application.dtos.BookDTO;
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
public class TestHTTPBooksAdapter {

	@Test
	public void listAllWhenEmpty() {
		Response response = rest(ContentType.JSON).get("/v1/books");

		Assertions.assertEquals(200, response.getStatusCode());
		Assertions.assertEquals("[]", response.getBody().asString());
	}

	@Test
	public void listAllWhenExists() throws Exception {
		rest(ContentType.JSON).body("{\"name\":\"author1\"}").post("/v1/authors").then().statusCode(201);
		rest(ContentType.JSON).body("{\"name\":\"author2\"}").post("/v1/authors").then().statusCode(201);

		rest(ContentType.JSON).body(
				"{\"isbn\":\"isbn1\",\"title\":\"title1\",\"authors\":[\"author1\"],\"description\":\"description1\"}")
				.post("/v1/books").then().statusCode(201);
		rest(ContentType.JSON).body(
				"{\"isbn\":\"isbn2\",\"title\":\"title2\",\"authors\":[\"author2\"],\"description\":\"description2\"}")
				.post("/v1/books").then().statusCode(201);

		Response response = rest(ContentType.JSON).get("/v1/books");

		Assertions.assertEquals(200, response.getStatusCode());
		ObjectMapper om = new ObjectMapper();
		Assertions.assertEquals(om.readTree("["
				+ "{\"isbn\":\"isbn1\",\"title\":\"title1\",\"authors\":[\"author1\"],\"description\":\"description1\"}"
				+ ","
				+ "{\"isbn\":\"isbn2\",\"title\":\"title2\",\"authors\":[\"author2\"],\"description\":\"description2\"}"
				+ "]"), om.readTree(response.jsonPath().prettify()));

//        Assertions.assertNotNull(response.getBody().asString());
//        Assertions.assertEquals(2, response.jsonPath().getList("$").size());
//		  List<DeserializableBookDTO> responseBooks = response.jsonPath().getList("", DeserializableBookDTO.class);
//		  Assertions.assertEquals(2, responseBooks.size());
	}

	@Test
	public void listAllWhenExists2() throws Exception {
		AuthorDTO a1 = new AuthorDTO("author1");
		AuthorDTO a2 = new AuthorDTO("author2");
		Arrays.asList(a1, a2).forEach(a -> {
			rest(ContentType.JSON).body(a).post("/v1/authors");
		});
		BookDTO b1 = new BookDTO("isbn1", "title1", new HashSet<>(Arrays.asList(a1.getName())), "description1");
		BookDTO b2 = new BookDTO("isbn2", "title2", new HashSet<>(Arrays.asList(a2.getName())), "description2");
		Arrays.asList(b1, b2).forEach(b -> {
			rest(ContentType.JSON).body(b).post("/v1/books");
		});

		Response response = rest(ContentType.JSON).get("/v1/books");

		Assertions.assertEquals(200, response.getStatusCode());
		Assertions.assertNotNull(response.jsonPath().getString(""));
		ObjectMapper om = new ObjectMapper();
		Assertions.assertEquals(om.readTree(om.writeValueAsString(Arrays.asList(b1, b2))),
				om.readTree(response.jsonPath().prettify()));
	}

	@Test
	public void getOneWhenEmpty() {
		Response response = rest(ContentType.JSON).get("/v1/books/unexistent_isbn");

		Assertions.assertEquals(404, response.getStatusCode());
		Assertions.assertEquals("", response.getBody().asString());
	}

	@Test
	public void getOneWhenExist() throws Exception {
		AuthorDTO a1 = new AuthorDTO("author1");
		AuthorDTO a2 = new AuthorDTO("author2");
		Arrays.asList(a1, a2).forEach(a -> {
			rest(ContentType.JSON).body(a).post("/v1/authors");
		});
		BookDTO b1 = new BookDTO("isbn1", "title1", new HashSet<>(Arrays.asList(a1.getName())), "description1");
		BookDTO b2 = new BookDTO("isbn2", "title2", new HashSet<>(Arrays.asList(a2.getName())), "description2");
		Arrays.asList(b1, b2).forEach(b -> {
			rest(ContentType.JSON).body(b).post("/v1/books");
		});

		Response response = rest(ContentType.JSON).get("/v1/books/isbn1");

		Assertions.assertEquals(200, response.getStatusCode());
		ObjectMapper om = new ObjectMapper();
		Assertions.assertEquals(om.readTree(om.writeValueAsString(b1)), om.readTree(response.jsonPath().prettify()));
	}

	@Test
	public void post() throws Exception {
		AuthorDTO a1 = new AuthorDTO("author1");
		rest(ContentType.JSON).body(a1).post("/v1/authors");
		BookDTO b1 = new BookDTO("isbn1", "title1", new HashSet<>(Arrays.asList(a1.getName())), "description1");

		Response response = rest(ContentType.JSON).body(b1).post("/v1/books");

		Assertions.assertEquals(201, response.getStatusCode());
		ObjectMapper om = new ObjectMapper();
		Assertions.assertEquals(om.readTree(om.writeValueAsString(b1)), om.readTree(response.jsonPath().prettify()));
	}

	@Test
	public void postWhenAlreadyExist() {
		AuthorDTO a1 = new AuthorDTO("author1");
		AuthorDTO a2 = new AuthorDTO("author2");
		Arrays.asList(a1, a2).forEach(a -> {
			rest(ContentType.JSON).body(a).post("/v1/authors");
		});
		BookDTO b1 = new BookDTO("isbn1", "title1", new HashSet<>(Arrays.asList(a1.getName())), "description1");
		BookDTO b2 = new BookDTO("isbn2", "title2", new HashSet<>(Arrays.asList(a2.getName())), "description2");
		Arrays.asList(b1, b2).forEach(b -> {
			rest(ContentType.JSON).body(b).post("/v1/books");
		});

		BookDTO requestBody = new BookDTO("isbn1", "title1", new HashSet<>(Arrays.asList(a1.getName())),
				"description1");
		Response response = rest(ContentType.JSON).body(requestBody).post("/v1/books");

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
		entityManager.createNativeQuery("DELETE FROM book_author").executeUpdate();
		entityManager.createNativeQuery("DELETE FROM book").executeUpdate();
		entityManager.createNativeQuery("DELETE FROM author").executeUpdate();
	}
}