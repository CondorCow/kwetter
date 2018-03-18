/*
 * Copyright (c) 2018.
 * Danny Janssen
 */

package controllers;

import com.google.gson.Gson;
import domain.Kweet;
import support.ResponseBody;
import services.KweetService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Path("/kweet")
@Produces(MediaType.APPLICATION_JSON)
public class KweetController {
    @Inject
    private KweetService service;
    private Gson gson = new Gson();

    @GET @Path("/{id}")
    public Response getKweet(@PathParam("id") long id) {
        Kweet kweet = service.getKweet(id);

        if (kweet == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        kweet.setUser(null);

        return Response.ok(gson.toJson(kweet)).build();
    }

    @POST @Consumes(MediaType.APPLICATION_JSON)
    public Response newKweet(Kweet kweet) {
        Kweet postedKweet = service.newKweet(new Kweet(kweet.getText(), kweet.getUser()));
        if (postedKweet == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok(postedKweet).build();
    }

    @PUT @Consumes(MediaType.APPLICATION_JSON)
    public Response updateKweet(Kweet kweet) {
        Kweet editedKweet = service.editKweet(kweet);

        if (editedKweet == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        editedKweet.setUser(null);

        return Response.ok(gson.toJson(kweet)).build();
    }

    @DELETE @Path("/{id}")
    public Response deleteKweet(@PathParam("id") long id) {
        service.deleteKweet(id);
        return Response.ok(new ResponseBody(true, "Success")).build();
    }

    @GET @Path("/user/{id}")
    public Response getKweetsOfUser(@PathParam("id") long id) {
        List<Kweet> kweets = service.getUserKweets(id);

        if (kweets == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(kweets).build();
    }

    @GET @Path("/timeline/{id}")
    public Response getFullTimeline(@PathParam("id") long id) {
        return Response.ok(service.getFullTimeline(id)).build();
    }


}
