package com.example.bookstore.domain.aggregate;

import com.example.bookstore.domain.command.PublishBookCommand;
import com.example.bookstore.domain.event.BookPublishedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookOrderTest {

    private AggregateTestFixture<BookOrder> fixture;

    @BeforeEach
    void setUp() {
        this.fixture = new AggregateTestFixture<>(BookOrder.class);
    }

    @Test
    void publishNewBook() {
        final String isbn = "012345";
        final String title = "Title of the Book";
        final String author = "Brook Bookworm";
        final double price = 12.34;
        final int numberOfSamples = 10;

        final PublishBookCommand publishBookCommand = PublishBookCommand.builder()
                .isbn(isbn)
                .title(title)
                .author(author)
                .price(price)
                .numberOfSamples(numberOfSamples)
                .build();
        final BookPublishedEvent expectedBookPublishedEvent = BookPublishedEvent.builder()
                .isbn(isbn)
                .title(title)
                .author(author)
                .price(price)
                .numberOfSamples(numberOfSamples)
                .build();

        this.fixture
                .given()
                .when(publishBookCommand)
                .expectEvents(expectedBookPublishedEvent);
    }
}