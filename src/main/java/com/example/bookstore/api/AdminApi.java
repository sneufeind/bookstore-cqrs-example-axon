package com.example.bookstore.api;

import com.example.bookstore.domain.command.PublishBookCommand;
import com.example.bookstore.domain.model.Book;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/admin")
public class AdminApi {

    private final CommandGateway commandGateway;

    @Autowired
    AdminApi(final CommandGateway commandGateway){
        this.commandGateway = commandGateway;
    }

    @PostMapping(value = "/publish", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> publishNewBook(final Book book){
        final CompletableFuture<Object> future = this.commandGateway.send( PublishBookCommand.builder()
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .numberOfSamples(book.getNumberOfSamples())
                .title(book.getTitle())
                .build() );

        return future.isCompletedExceptionally()
                ? ResponseEntity.unprocessableEntity().build()
                : ResponseEntity.ok().build();
    }
}
