package com.example.bookstore.domain.event;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@RequiredArgsConstructor
public class BookOrderedEvent {

    @TargetAggregateIdentifier
    private final String isbn;
    private final String orderId;
    private final Integer samples;
    private final Double price;
}
