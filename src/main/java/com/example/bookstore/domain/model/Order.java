package com.example.bookstore.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {

    private String orderId;
    private String isbn;
    private Integer amount;
    private Double price;
}
