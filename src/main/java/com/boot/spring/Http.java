package com.boot.spring;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by darlan on 26/01/17.
 */
public interface Http {

    static void ok(@Suspended AsyncResponse response, Object data) {
        response.resume(Response.ok()
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(data)
                .build());
    }

    static void internalServerError(@Suspended AsyncResponse response) {
        response.resume(Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .build());
    }

}
