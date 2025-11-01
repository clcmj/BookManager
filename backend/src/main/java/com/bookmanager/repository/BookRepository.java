package com.bookmanager.repository;

import com.bookmanager.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByCategoryId(Long categoryId);
    List<Book> findByTitleContainingOrAuthorContainingOrIsbnContaining(String title, String author, String isbn);
    List<Book> findTop10ByOrderByBorrowCountDesc();
    List<Book> findTop10ByOrderByCreatedAtDesc();
}