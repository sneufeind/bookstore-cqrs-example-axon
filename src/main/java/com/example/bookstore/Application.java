package com.example.bookstore;

import com.example.bookstore.domain.command.PublishBookCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private CommandGateway commandGateway;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(final String... args) throws Exception {
		Arrays.asList(
			PublishBookCommand.builder()
					.author("Eric J. Evans")
					.title("Domain-Driven Design: Tackling Complexity in the Heart of Software")
					.isbn("978-0-321-12521-7")
					.price(48.99)
					.numberOfSamples(3)
					.build(),
			PublishBookCommand.builder()
					.author("Vaughn Vernon")
					.title("Domain-Driven Design Distilled ")
					.isbn("978-0-13-443442-1")
					.price(26.99)
					.numberOfSamples(10)
					.build(),
			PublishBookCommand.builder()
					.author("Scott Millett")
					.title("Patterns, Principles, and Practices of Domain-Driven Design")
					.isbn("978-1-118-71470-6")
					.price(42.26)
					.numberOfSamples(2)
					.build()
		).stream().forEach(command -> this.commandGateway.send(command));
	}
}
