package com.boot.spring.app;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;


/**
 * Created by darlan on 25/01/17.
 */
@Configuration
public class SpringBootVertxConfiguration {

    @Autowired
    private Environment environment;

    public int httpPort() {
        return environment.getProperty("http.port", Integer.class, 8080);
    }

    @Bean
    @DependsOn("vertx")
    MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost");
    }

    @Bean
    Vertx vertx() {
        return Vertx.vertx(new VertxOptions().setWorkerPoolSize(400));
    }

}