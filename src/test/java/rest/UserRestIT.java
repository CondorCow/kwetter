/*
 * Copyright (c) 2018.
 * Danny Janssen
 */

package rest;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserRestIT {
    private int OK = 200;
    private int NOT_FOUND = 404;

    @Test
    public void getUserWithEmptyUsernameShouldReturnMethodNotAllowed() {
        given().when().get("/kwetterdanny/api/user/").then().statusCode(405);
    }

    @Test
    public void addUserWithValidRequestBodyShouldReturnOkWithId() {
        String json = "{ \"username\": \"testusername\", \"password\": \"password\" }";
        given().contentType("application/json").body(json)
                .when().post("/kwetterdanny/api/user")
                .then().statusCode(OK)
                .body("$", hasKey("id"));
    }

    @Test
    public void addUserWithEmptyRequestBodyShouldReturnNotFound() {
        given().contentType("application/json").body("{}")
                .when().post("/kwetterdanny/api/user")
                .then().statusCode(NOT_FOUND);
    }

    @Test
    public void editUserWithValidRequestBodyShouldReturnOk() {
        String json = "{ \"username\": \"dannyajc\", \"bio\": \"hallo allemaal\" }";
        given().contentType("application/json").body(json)
                .when().put("/kwetterdanny/api/user")
                .then().statusCode(OK)
                .body("bio", equalTo("hallo allemaal"));
    }

    @Test
    public void editUserWithUnknownUsernameShouldReturnNotFound() {
        String json = "{ \"username\": \"unknown\", \"bio\": \"My new bio\" }";
        given().contentType("application/json").body(json)
                .when().put("/kwetterdanny/api/user")
                .then().statusCode(NOT_FOUND);
    }

    @Test
    public void authenticateResponseShouldReturnTrueIfPasswordCorrect() {
        String json = "{ \"username\": \"dannyajc\", \"password\": \"jea6\" }";
        given().contentType("application/json").body(json)
                .when().post("/kwetterdanny/api/user/auth")
                .then().statusCode(OK)
                .body("response", equalTo(true),
                        "message", equalTo("authorized"));
    }

    @Test
    public void authenticateResponseShouldReturnFalseIfPasswordIncorrect() {
        String json = "{ \"username\": \"unknown\", \"password\": \"unknown\" }";
        given().contentType("application/json").body(json)
                .when().post("/kwetterdanny/api/user/auth")
                .then().statusCode(401);
    }

    @Test
    public void getUsersShouldReturnArrayAndOk() {
        given()
                .when().get("/kwetterdanny/api/users")
                .then().statusCode(OK)
                .body("$", not(hasSize(0)));
    }

    @Test
    public void followingResponseShouldReturnTrueIfUsersExist() {
        given()
                .when().post("/kwetterdanny/api/user/1/follow/5")
                .then().statusCode(OK)
                .body("response", equalTo(true));
    }

    @Test
    public void followingResponseShouldReturnFalseIfUsersDontExist() {
        given()
                .when().post("/kwetterdanny/api/user/1/follow/2")
                .then().statusCode(OK)
                .body("response", equalTo(false));
    }

    @Test
    public void editRoleWithValidRequestBodyShouldReturnOk() {
        String json = "{ \"id\": \"1\", \"role\": \"MODERATOR\" }";
        given().contentType("application/json").body(json)
                .when().put("/kwetterdanny/api/user/role")
                .then().statusCode(OK)
                .body("role", equalTo("MODERATOR"));
    }

    @Test
    public void editRoleWithUnknownRoleShouldReturnNotFound() {
        String json = "{ \"id\": \"1\", \"role\": \"unknown\" }";
        given().contentType("application/json").body(json)
                .when().put("/kwetterdanny/api/user/role")
                .then().statusCode(400);
    }
}
