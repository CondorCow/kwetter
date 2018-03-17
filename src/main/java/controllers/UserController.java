package controllers;

import domain.User;
import support.ResponseBody;
import services.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {
    @Inject
    private UserService service;

    @GET
    @Path("/{username}")
    public Response getUser(@PathParam("username") String username) {
        User user = service.getUserByUsername(username);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(user.serialized()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {
        User createdUser = service.newUser(user.getUsername(), user.getPassword());

        if (createdUser == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(createdUser).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editUser(User user) {
        User editedUser = service.updateUser(user);

        if (editedUser == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(editedUser.serialized()).build();
    }

    @POST
    @Path("/auth")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticate(User user) {
        if (service.authorizeUser(user)) {
            return Response.ok(new ResponseBody(true, "authorized")).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/{id}/follow/{followingId}")
    public Response followUser(@PathParam("id") long id, @PathParam("followingId") long followingId) {
        boolean result = service.followUser(id, followingId);
        String errorMessage = null;
        if(!result)
            errorMessage = "User does not exist / User already follows this person";

        return Response.ok(new ResponseBody(result, errorMessage)).build();
    }

    @POST
    @Path("/{id}/unfollow/{unfollowingId}")
    public Response unfollowUser(@PathParam("id") long id, @PathParam("unfollowingId") long unfollowingId) {
        boolean result = service.unfollowUser(id, unfollowingId);
        String errorMessage = null;
        if(!result)
            errorMessage = "User does not exist / User doesn't follow this person and can therefore not unfollow him/her";

        return Response.ok(new ResponseBody(result, errorMessage)).build();
    }

    @GET
    @Path("/{id}/following")
    public Response getFollowing(@PathParam("id") long id) {
        return Response.ok(service.getFollowing(id)).build();
    }

    @GET
    @Path("/{id}/followers")
    public Response getFollowers(@PathParam("id") long id) {
        return Response.ok(service.getFollowers(id)).build();
    }

    @PUT
    @Path("/role")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editRole(User user) {
        User userWithNewRole = service.editRole(user.getId(), user.getRole());

        if (userWithNewRole == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok(userWithNewRole.serialized()).build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeUser(@PathParam("id") long id) {
        service.deleteUser(id);
        return Response.ok(new ResponseBody(true, null)).build();
    }
}
