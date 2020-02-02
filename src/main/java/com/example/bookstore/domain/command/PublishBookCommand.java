package com.example.bookstore.domain.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class PublishBookCommand {

    @TargetAggregateIdentifier
    private final String isbn;
    private final String title;
    private final String author;
    private final Double price;
    private final Integer numberOfSamples;
}
