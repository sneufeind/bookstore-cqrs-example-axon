package com.example.bookstore.domain.projection;

import com.example.bookstore.domain.event.BookOrderedEvent;
import com.example.bookstore.domain.event.BookPublishedEvent;
import com.example.bookstore.domain.model.Order;
import com.example.bookstore.domain.model.OrderResponse;
import com.example.bookstore.domain.query.FindAvailableNumberOfSamplesForBookQuery;
import com.example.bookstore.domain.query.FindOrderQuery;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemOrderService {

    private final Map<String, Double> priceRepository = new ConcurrentHashMap<>();
    private final Map<String, Integer> stockRepository = new ConcurrentHashMap<>();
    private final List<Order> orders = Collections.synchronizedList(new LinkedList<>());

    public InMemOrderService(){}

    @EventHandler
    public void on(final BookPublishedEvent event){
        this.priceRepository.put(event.getIsbn(), event.getPrice());
        this.stockRepository.put(event.getIsbn(), event.getNumberOfSamples());
    }

    @EventHandler
    public void on(final BookOrderedEvent event){
        final String isbn = event.getIsbn();
        if (!this.priceRepository.containsKey(isbn)) {
            throw new IllegalArgumentException(String.format("Book with ISBN='%s' not registered!", isbn == null ? "null" : isbn) );
        }
        final Double price = this.priceRepository.get(isbn);

        this.orders.add( Order.builder()
                .amount(event.getSamples())
                .price(event.getSamples() * price)
                .isbn(isbn)
                .build() );
        if (this.stockRepository.containsKey(isbn)) {
            final Integer current = this.stockRepository.get(isbn);
            this.stockRepository.put(isbn, current - event.getSamples());
        }
    }

    @QueryHandler
    public Integer handle(final FindAvailableNumberOfSamplesForBookQuery query){
        return this.stockRepository.getOrDefault(query.getIsbn(), 0);
    }

    @QueryHandler
    public Optional<OrderResponse> handle(final FindOrderQuery query){
        final String orderId = query.getOrderId();
        return this.orders.stream()
                .filter(order -> order.getOrderId().equals(orderId))
                .map(order -> OrderResponse.builder()
                        .orderId(orderId)
                        .price(order.getPrice())
                        .build())
                .findFirst();
    }
}
