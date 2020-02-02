package com.example.bookstore.domain.command;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@RequiredArgsConstructor
public class OrderBookCommand {

    @TargetAggregateIdentifier
    private final String isbn;
    private final Integer samples;
}
