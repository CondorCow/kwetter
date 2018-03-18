/*
 * Copyright (c) 2018.
 * Danny Janssen
 */

package rest;

import io.restassured.RestAssured;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class KweetRestIT {
    private int OK = 200;
    private int NOT_FOUND = 404;

    @Test
    public void postKweetWithValidRequestBodyShouldReturnOk() {
        String json = "{ \"text\": \"kweet message\", \"user\": { \"id\": \"1\" } }";
        RestAssured.given().contentType("application/json").body(json)
                .when().post("/kwetter/api/kweet")
                .then().statusCode(OK)
                .body("id", not(0), "text", equalTo("kweet message"));
    }

    @Test
    public void postKweetWithEmptyTextShouldReturnBadRequest() {
        String json = "{ \"text\": \"\", \"user\": { \"id\": \"1\" } }";
        RestAssured.given().contentType("application/json").body(json)
                .when().post("/kwetter/api/kweet")
                .then().statusCode(400); //Bad request
    }

    @Test
    public void getKweetShouldReturnOk() {
        given().contentType("application/json")
                .when().get("/kwetter/api/kweet/4")
                .then().statusCode(OK)
                .body("$", hasKey("text"));
    }

    @Test
    public void getUnknownKweetShouldReturnNotFound() {
        given().contentType("application/json")
                .when().get("/kwetter/api/kweet/100")
                .then().statusCode(NOT_FOUND);
    }

    @Test
    public void editKweetWithValidRequestBodyShouldReturnOk() {
        String json = "{ \"id\": \"4\", \"text\": \"Excuses #respect\" }";
        given().contentType("application/json").body(json)
                .when().put("/kwetter/api/kweet")
                .then().statusCode(OK)
                .body("text", equalTo("Excuses #respect"));
    }

    @Test
    public void editKweetWithUnknownUserShouldReturnNotFound() {
        String json = "{ \"text\": \"Excuses #respect\", \"id\": \"100\" }";
        given().contentType("application/json").body(json)
                .when().put("/kwetter/api/kweet")
                .then().statusCode(NOT_FOUND);
    }

    @Test
    public void getKweetsOfValidUserShouldReturnArrayAndOk() {
        given()
                .when().get("/kwetter/api/kweet/user/1")
                .then().statusCode(OK)
                .body("$", not(hasSize(0)));
    }

    @Test
    public void getKweetsOfInvalidUserShouldReturnNotFound() {
        given()
                .when().get("/kwetter/api/kweet/user/100")
                .then().statusCode(NOT_FOUND);
    }

    @Test
    public void getTimelineShouldReturnArrayAndOk() {
        given()
                .when().get("/kwetter/api/kweet/user/1")
                .then().statusCode(OK)
                .body("$", not(hasSize(0)));
    }

    @Test
    public void getTimelineWithUnknownUserShouldReturnNotFound() {
        given()
                .when().get("/kwetter/api/kweet/user/100")
                .then().statusCode(NOT_FOUND);
    }
}
