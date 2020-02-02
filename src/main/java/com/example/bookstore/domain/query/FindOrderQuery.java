package com.example.bookstore.domain.query;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FindOrderQuery {

    private final String orderId;
}
