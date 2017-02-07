package com.boot.spring;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * Created by darlan on 25/01/17.
 */
@EnableAutoConfiguration
@SpringBootApplication
public class AppBoot {

	private static final Logger log = LoggerFactory.getLogger(AppBoot.class);
	@Autowired
	private Environment environment;
	@Autowired
	private Vertx vertx;
	@Autowired
	private VertxHttpServer springBootVertxHttpServer;
	@Autowired
	private PersonRepository personRepository;

	public static void main(String[] args) {
		SpringApplication.run(AppBoot.class, args);
	}

	@PostConstruct
	public void deployVerticle() {
		vertx.deployVerticle(springBootVertxHttpServer);
		vertx.deployVerticle(personRepository);
	}

	String host() {
		return environment.getProperty("MONGO_HOST");
	}

	String port() {
		return environment.getProperty("MONGO_PORT");
	}

	@Bean
	@DependsOn("vertx")
	MongoClient mongoClient() {
		String connectionUrl = "mongodb://" + host() + ":" + port();
		log.info("Trying to connection on -> {}", connectionUrl);
		return MongoClients.create(connectionUrl);
	}

	@Bean
	Vertx vertx() {
		return Vertx.vertx(new VertxOptions().setWorkerPoolSize(400));
	}

}
