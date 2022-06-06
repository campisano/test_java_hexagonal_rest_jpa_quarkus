package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class TestUtils {
    private static ObjectMapper om = new ObjectMapper();

    public static JsonNode toJson(String jsonString) throws Exception {
        return om.readTree(jsonString);
    }

    public static JsonNode toJson(Object object) throws Exception {
        return toJson(om.writeValueAsString(object));
    }

    public static RequestSpecification JsonRequest() {
        return RestAssured.given().contentType(ContentType.JSON);
    }
}
