package com.boot.spring.person;

import com.mongodb.Block;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoCollection;
import com.boot.spring.app.RepositoryAction;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by darlan on 25/01/17.
 */
@Component
public class PersonRepository extends AbstractVerticle {

    @Autowired
    private ApplicationContext context;
    private MongoCollection<Document> collection;

    @PostConstruct
    public void onStart() {
        collection = context.getBean(MongoClient.class)
                .getDatabase("db")
                .getCollection("person");
    }

    @Override
    public void start() throws Exception {
        vertx.eventBus().<JsonObject>consumer("person", msg -> {
            List<RepositoryAction<JsonObject>> list = new ArrayList<>();
            list.add(new ListRepositoryAction());
            list.add(new PostRepositoryAction());
            Optional<RepositoryAction<JsonObject>> executed =
                    list.stream().filter(action -> action.execute(msg)).findFirst();
            System.out.println("Executed: " + executed);
        });
    }

    public class ListRepositoryAction implements RepositoryAction<JsonObject> {
        @Override
        public boolean execute(Message<JsonObject> msg) {
            boolean matches;
            if((matches = matches(msg, "list"))) {
                List<JsonObject> list = new ArrayList<>();
                Block<Document> convert = document -> list.add(new JsonObject(document.toJson()));
                SingleResultCallback<Void> callback = (result, exception) -> {
                    if (exception == null) {
                        msg.reply(new JsonArray(list));
                        return;
                    }
                    onError(msg, exception);
                };
                collection.find().forEach(convert, callback);
            }
            return matches;
        }
    }

    public class PostRepositoryAction implements RepositoryAction<JsonObject> {
        @Override
        public boolean execute(Message<JsonObject> msg) {
            boolean matches;
            if((matches = matches(msg, "post"))) {
                JsonObject data = msg.body().getJsonObject("data");
                Document document = Document.parse(data.encode());
                collection.insertOne(document, (result, exception) -> {
                    if(exception == null) {
                        msg.reply(new JsonObject(document.toJson()));
                        return;
                    }
                    onError(msg, exception);
                });
            }
            return matches;
        }
    }

}
