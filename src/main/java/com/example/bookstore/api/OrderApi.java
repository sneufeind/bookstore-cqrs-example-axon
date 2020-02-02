package com.example.bookstore.api;

import com.example.bookstore.domain.command.OrderBookCommand;
import com.example.bookstore.domain.model.Book;
import com.example.bookstore.domain.model.OrderRequest;
import com.example.bookstore.domain.model.OrderResponse;
import com.example.bookstore.domain.query.FindAllBooksQuery;
import com.example.bookstore.domain.query.FindAvailableNumberOfSamplesForBookQuery;
import com.example.bookstore.domain.query.FindOrderQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@RestController
@RequestMapping("/api")
public class OrderApi {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @Autowired
    OrderApi(final CommandGateway commandGateway, final QueryGateway queryGateway){
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @GetMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
    private Future<List<Book>> getBooks(){
        return this.queryGateway.query(
                new FindAllBooksQuery(),
                ResponseTypes.multipleInstancesOf(Book.class)
        );
    }

    @GetMapping(value = "/books/{isbn}", produces = MediaType.APPLICATION_JSON_VALUE)
    private Future<Integer> getBooks(@PathVariable("isbn") final String isbn){
        return this.queryGateway.query(
                new FindAvailableNumberOfSamplesForBookQuery(isbn),
                ResponseTypes.instanceOf(Integer.class)
        );
    }

    @PostMapping(value = "/orders", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Future<OrderResponse> order(final OrderRequest orderRequest){
        final CompletableFuture<String> orderIdFuture = this.commandGateway.send( new OrderBookCommand(orderRequest.getIsbn(), orderRequest.getAmount()) );
        if (orderIdFuture.isCompletedExceptionally()) {
            ResponseEntity.unprocessableEntity().build();
        }
        try {
            final String orderId = orderIdFuture.get();
            return this.queryGateway.query(
                    new FindOrderQuery(orderId),
                    ResponseTypes.instanceOf(OrderResponse.class)
            );
        } catch (final InterruptedException | ExecutionException e) {
            log.error("An error occured!", e);
            return CompletableFuture.failedFuture(e);
        }
    }
}
