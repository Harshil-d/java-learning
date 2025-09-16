package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() { return bookRepository.findAll(); }

    public Book findById(Long id) { return bookRepository.findById(id).orElseThrow(); }

    public Book save(Book book) { return bookRepository.save(book); }

    public Book update(Long id, Book book) {
        Book existing = findById(id);
        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setPrice(book.getPrice());
        return bookRepository.save(existing);
    }

    public void delete(Long id) { bookRepository.deleteById(id); }
}
