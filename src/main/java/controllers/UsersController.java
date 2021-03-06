/*
 * Copyright (c) 2018.
 * Danny Janssen
 */

package controllers;

import services.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersController {
    @Inject
    private UserService service;

    @GET
    public Response getAllUsers() {
        return Response.ok(service.getAllUsers()).build();
    }
}