package com.bookmanager.service;

import com.bookmanager.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookService {
    Book addBook(Book book);
    Optional<Book> findById(Long bookId);
    List<Book> findAll();
    List<Book> findByCategoryId(Long categoryId);
    List<Book> search(String keyword);
    List<Book> findPopularBooks();
    List<Book> findNewBooks();
    Book updateBook(Long bookId, Book bookDetails);
    void deleteBook(Long bookId);
    Book updateBookStatus(Long bookId, Integer status);
    boolean decreaseStock(Long bookId);
    void increaseStock(Long bookId);
}