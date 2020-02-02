package com.example.bookstore.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Book {
    private String author;
    private String isbn;
    private String title;
    private Integer numberOfSamples;
    private Double price;
}
