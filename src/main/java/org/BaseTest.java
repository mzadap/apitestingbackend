package org;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class BaseTest {

    public static RequestSpecification settingBaseUrl() {
        RequestSpecification request = RestAssured.given();
        request.baseUri("https://api.github.com");
        request.header("Accept", "application/vnd.github+json");
        request.header("Authorization", "Bearer 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");
        request.header("X-GitHub-Api-Version", "2022-11-28");
        return request;
    }

    public String createRepository(File body, int statusCode) {
        String response = RestAssured.given()
                .log()
                .all()
                .spec(settingBaseUrl())
                .body(body)
                .when()
                .post("/user/repos")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(statusCode)
                .extract()
                .response()
                .asString();
        return response;
    }

    public String createRepository(String body, int statusCode, String token) {
        RestAssured.baseURI = "https://api.github.com";
        String response = RestAssured.given()
                .log()
                .all()
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .body(body)
                .when()
                .post("/user/repos")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(statusCode)
                .extract()
                .response()
                .asString();
        return response;
    }

    public String createRepository1(File body, int statusCode, String endpoint) {
        String response = RestAssured.given()
                .log()
                .all()
                .spec(settingBaseUrl())
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(statusCode)
                .extract()
                .response()
                .asString();
        return response;
    }

    public String getIssue(String issueNumber, int statusCode) {
        String response = RestAssured.given()
                .log()
                .all()
                .spec(settingBaseUrl())
                .when()
                .get("/repos/mzadap/Demo-repo-testing/issues/" +issueNumber)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(statusCode)
                .extract()
                .response()
                .asString();
        return response;
    }

    public String createIssue(int statusCode, Object classObject) {
        String body = new Gson().toJson(classObject);
        String response = RestAssured.given()
                .log()
                .all()
                .spec(settingBaseUrl())
                .body(body)
                .when()
                .post("/repos/mzadap/Demo-repo-testing/issues")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(statusCode)
                .extract()
                .response()
                .asString();
        return response;
    }

    public String updateIssue(int statusCode, Object classObject, int issueNumber) {
        String body = new Gson().toJson(classObject);
        String response = RestAssured.given()
                .log()
                .all()
                .spec(settingBaseUrl())
                .body(body)
                .when()
                .patch("/repos/mzadap/Demo-repo-testing/issues/" +issueNumber)
                .then()
                .log()
                .all()
                    .assertThat()
                .statusCode(statusCode)
                .extract()
                .response()
                .asString();
        return response;
    }

    public <T> T getObjectFromJson(String jsonFile, Class<T> tClass)  {
        try {
            return new ObjectMapper().readValue(getFile(jsonFile), tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFile(String file) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(file);
        assert is != null;
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }
}
