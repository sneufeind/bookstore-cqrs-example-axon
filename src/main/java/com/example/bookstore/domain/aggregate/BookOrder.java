package com.example.bookstore.domain.aggregate;

import com.example.bookstore.domain.command.OrderBookCommand;
import com.example.bookstore.domain.command.PublishBookCommand;
import com.example.bookstore.domain.event.BookOrderedEvent;
import com.example.bookstore.domain.event.BookPublishedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

@Aggregate
@Slf4j
public class BookOrder {

    @AggregateIdentifier
    private String isbn;
    private Integer numberOfAvailableSamples;
    private Double price;

    @CommandHandler
    public BookOrder(final PublishBookCommand command){
        // Validation
        if (command.getIsbn() == null) {
            throw new IllegalArgumentException("ISBN must be set!");
        } else if(command.getTitle() == null || command.getTitle().isBlank()) {
            throw new IllegalArgumentException("Book title must be set!");
        } else if(command.getNumberOfSamples() == null || command.getNumberOfSamples().intValue() < 0) {
            throw new IllegalArgumentException("The number of samples of this book must be set!");
        }

        AggregateLifecycle.apply( BookPublishedEvent.builder()
                .author(command.getAuthor())
                .price(command.getPrice())
                .isbn(command.getIsbn())
                .numberOfSamples(command.getNumberOfSamples())
                .title(command.getTitle())
                .build() );
    }

    @EventSourcingHandler
    public void on(final BookPublishedEvent event){
        // set field values
        this.isbn = event.getIsbn();
        this.numberOfAvailableSamples = event.getNumberOfSamples();
        this.price = event.getPrice();

        log.info("Create book with ISBN={}.", this.isbn);
    }

    @CommandHandler
    public String handle(final OrderBookCommand command){
        // Validation
        if(command.getSamples() > this.numberOfAvailableSamples){
            throw new IllegalArgumentException(String.format("For ISBN=%s only %d samples are available!", this.isbn, this.numberOfAvailableSamples));
        }

        final String orderId = UUID.randomUUID().toString();
        AggregateLifecycle.apply( new BookOrderedEvent(
                command.getIsbn(),
                orderId,
                command.getSamples(),
                this.price
        ) );
        return orderId;
    }

    @EventSourcingHandler
    public void on(final BookOrderedEvent event){
        this.numberOfAvailableSamples =- event.getSamples(); // update stock
    }
}
