/*
 * Copyright (c) 2018.
 * Danny Janssen
 */

package rest;

import io.restassured.RestAssured;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserApiTest {

    private String API_URL = "/kwetterdanny/api";
    private int OK = 200;
    private int NOT_FOUND = 404;



    @Test
    public void updateUserTest() {
        String json = "{ \"username\": \"dannyajc\", \"bio\": \"hallo allemaal\" }";
        given().contentType("application/json").body(json)
                .when().put(API_URL + "/user")
                .then().statusCode(OK)
                .body("bio", equalTo("hallo allemaal"));
    }

    @Test
    public void changeRoleTest() {
        String json = "{ \"id\": \"1\", \"role\": \"MOD\" }";
        given().contentType("application/json").body(json)
                .when().put(API_URL + "/user/role")
                .then().statusCode(OK)
                .body("role", equalTo("MOD"));
    }

    @Test
    public void updateNotExistingUserTest() {
        String json = "{ \"username\": \"obama\"," +
                        " \"email\": \"kaas@ham.nl\" }";
        given().contentType("application/json").body(json)
                .when().put(API_URL + "/user")
                .then().statusCode(NOT_FOUND);
    }

    @Test
    public void newUserTest() {
        String json = "{ \"username\": \"donaldtrump\", \"password\": \"makeamericagreatornot\" }";
        given().contentType("application/json").body(json)
                .when().post(API_URL + "/user")
                .then().statusCode(OK)
                .body("", hasKey("id"));
    }

    @Test
    public void newUserWithNoParamsTest() {
        given().contentType("application/json").body("{}")
                .when().post(API_URL + "/user")
                .then().statusCode(NOT_FOUND);
    }

    @Test
    public void getAllUsersTest() {
        given()
                .when().get(API_URL + "/users")
                .then().statusCode(OK)
                .body("", not(hasSize(1)));
    }

    @Test
    public void followAndUnfollowTest() {
        // Exists
        given()
                .when().post(API_URL + "/user/1/follow/6")
                .then().statusCode(OK)
                .body("response", equalTo(true));

        // Unfollow
        RestAssured.given().when()
                .post(API_URL + "/user/1/unfollow/6").then().statusCode(OK).body("response", equalTo(true));

        // User doesn't exist

        given()
                .when().post(API_URL + "/user/1/follow/2")
                .then().statusCode(OK)
                .body("response", equalTo(false));
    }

    @Test
    public void authorizationTest() {
        //Good
        String json = "{ \"username\": \"dannyajc\", \"password\": \"jea6\" }";
        given().contentType("application/json").body(json)
                .when().post(API_URL + "/user/authorize")
                .then().statusCode(OK)
                .body("message", equalTo("authorized"));

        //Bad
        String json2 = "{ \"username\": \"unknown\", \"password\": \"unknown\" }";
        given().contentType("application/json").body(json2)
                .when().post(API_URL + "/user/authorize")
                .then().statusCode(401);
    }
//    @Test
//    public void fastUnfollow(){
//        RestAssured.given().when()
//                .post(API_URL + "/user/1/unfollow/6").then().statusCode(OK).body("response", equalTo(true));
//    }

}
