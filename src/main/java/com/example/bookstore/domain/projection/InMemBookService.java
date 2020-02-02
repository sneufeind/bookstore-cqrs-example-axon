package com.example.bookstore.domain.projection;

import com.example.bookstore.domain.event.BookPublishedEvent;
import com.example.bookstore.domain.model.Book;
import com.example.bookstore.domain.query.FindAllBooksQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class InMemBookService {

    private final Map<String, Book> bookRepository = new ConcurrentHashMap<>();

    public InMemBookService(){}

    @EventHandler
    public void on(final BookPublishedEvent event){
        final String isbn = event.getIsbn();

        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN must be set!");
        } else if (event.getNumberOfSamples() == null || event.getNumberOfSamples().intValue() < 0) {
            throw new IllegalArgumentException("Number of samples must be >= 0!");
        }

        this.bookRepository.put(isbn, Book.builder()
                .author(event.getAuthor())
                .isbn(isbn)
                .title(event.getTitle())
                .build() );
    }

    @QueryHandler
    public List<Book> handle(final FindAllBooksQuery query){
        return new ArrayList<>(this.bookRepository.values());
    }
}
