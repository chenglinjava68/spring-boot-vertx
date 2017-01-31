package com.boot.spring.person;

import com.boot.spring.app.GenericHttpService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by darlan on 25/01/17.
 */
@Component
public class PersonServiceImpl implements PersonService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void list(@Suspended AsyncResponse res,
                     @Context Vertx vertx) {
        vertx.eventBus().<JsonArray>send("person", new JsonObject()
                .put("action", "list"), async -> {
            if (async.succeeded()) {
                JsonArray data = async.result().body();
                GenericHttpService.ok(res, Person.fromJson(data));
                return;
            }
            GenericHttpService.internalServerError(res);
        });
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void post(@Suspended AsyncResponse res,
                     @Context Vertx vertx, Person person) {
        vertx.eventBus().<JsonObject>send("person", new JsonObject()
                .put("action", "post")
                .put("data", person.asJson()), async -> {
            if (async.succeeded()) {
                JsonObject data = async.result().body();
                GenericHttpService.ok(res, Person.fromJson(data));
                return;
            }
            GenericHttpService.internalServerError(res);
        });
    }

}