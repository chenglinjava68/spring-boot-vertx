package com.boot.spring.app;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * Created by darlan on 26/01/17.
 */
public interface RepositoryAction<T extends JsonObject> {

    default boolean matches(Message<T> msg, String expectation) {
        JsonObject body = msg.body();
        String action = body.getString("action");
        return action != null && expectation != null
                && action.equals(expectation);
    }

    default void onError(Message<T> msg, Throwable exception){
        msg.fail(0, exception.getMessage());
    }

    boolean execute(Message<T> msg);

}
