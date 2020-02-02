package com.example.bookstore.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {

    private String orderId;
    private Double price;
}
