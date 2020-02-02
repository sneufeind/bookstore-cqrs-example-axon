package com.example.bookstore.domain.model;

import lombok.Data;

@Data
public class OrderRequest {

    private String isbn;
    private int amount;
}
