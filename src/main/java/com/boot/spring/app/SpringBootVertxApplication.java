package com.boot.spring.app;

import com.boot.spring.person.PersonRepository;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * Created by darlan on 25/01/17.
 */
@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = "com.boot.spring")
public class SpringBootVertxApplication {

	@Autowired
	private Vertx vertx;
	@Autowired
	private SpringBootVertxHttpServer springBootVertxHttpServer;
	@Autowired
	private PersonRepository personRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootVertxApplication.class, args);
	}

	@PostConstruct
	public void deployVerticle() {
		vertx.deployVerticle(springBootVertxHttpServer);
		vertx.deployVerticle(personRepository);
	}

}
