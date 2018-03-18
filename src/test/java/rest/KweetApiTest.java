/*
 * Copyright (c) 2018.
 * Danny Janssen
 */

package rest;

import io.restassured.RestAssured;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class KweetApiTest {

    private String API_URL = "/kwetterdanny/api";
    private int OK = 200;
    private int NOT_FOUND = 404;

    @Test
    public void notExistingUserTest() {
        given()
                .when().get(API_URL + "/kweet/user/404")
                .then().statusCode(NOT_FOUND);
    }

    @Test
    public void timeLineTest() {
        given()
                .when().get(API_URL + "/kweet/user/1")
                .then().statusCode(OK)
                .body("$", not(hasSize(0)));
    }
    @Test
    public void getKweetTest() {
        given().contentType("application/json")
                .when().get(API_URL + "/kweet/3")
                .then().statusCode(OK)
                .body("", hasKey("text"));
    }

    @Test
    public void notFoundTest() {
        given().contentType("application/json")
                .when().get(API_URL + "/kweet/300")
                .then().statusCode(NOT_FOUND);
    }

    @Test
    public void newKweetTest() {
        String body = "{ \"text\": \"kweet message\", \"user\": { \"id\": \"1\" } }";
        RestAssured.given().contentType("application/json").body(body)
                .when().post(API_URL + "/kweet")
                .then().statusCode(OK)
                .body("id", not(0), "text", equalTo("kweet message"));

        // No message for kweet so should return bad request
        String body2 = "{ \"text\": \"\", \"user\": { \"id\": \"1\" } }";
        RestAssured.given().contentType("application/json").body(body2)
                .when().post(API_URL + "/kweet")
                .then().statusCode(400); //Bad request
    }

    //User has more than zero kweet posted
    @Test
    public void getKweetsTest() {
        RestAssured
                .given()
                .when()
                .get(API_URL + "/kweet/user/6")
                .then().statusCode(OK)
                .body("", not(hasSize(0)));
    }

    @Test
    public void changeKweetMessageTest() {
        String kweetMessage = "Kaas hoort bij een tosti";
        String body = "{ \"id\": \"3\", \"text\": \""+ kweetMessage +"\" }";
        given().contentType("application/json").body(body)
                .when().put(API_URL + "/kweet")
                .then().statusCode(OK)
                .body("text", equalTo(kweetMessage));
    }




}
