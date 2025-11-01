package com.bookmanager.service.impl;

import com.bookmanager.model.Book;
import com.bookmanager.model.Category;
import com.bookmanager.repository.BookRepository;
import com.bookmanager.repository.CategoryRepository;
import com.bookmanager.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Book addBook(Book book) {
        // 检查ISBN是否已存在
        if (bookRepository.findByTitleContainingOrAuthorContainingOrIsbnContaining(book.getIsbn(), "", "").size() > 0) {
            throw new IllegalArgumentException("ISBN已存在");
        }
        // 验证分类是否存在
        Optional<Category> optionalCategory = categoryRepository.findById(book.getCategory().getId());
        if (optionalCategory.isEmpty()) {
            throw new IllegalArgumentException("分类不存在");
        }
        book.setCategory(optionalCategory.get());
        book.setAvailableStock(book.getTotalStock());
        book.setBorrowCount(0);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> findById(Long bookId) {
        return bookRepository.findById(bookId);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findByCategoryId(Long categoryId) {
        return bookRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Book> search(String keyword) {
        return bookRepository.findByTitleContainingOrAuthorContainingOrIsbnContaining(keyword, keyword, keyword);
    }

    @Override
    public List<Book> findPopularBooks() {
        return bookRepository.findTop10ByOrderByBorrowCountDesc();
    }

    @Override
    public List<Book> findNewBooks() {
        return bookRepository.findTop10ByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional
    public Book updateBook(Long bookId, Book bookDetails) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setPublisher(bookDetails.getPublisher());
            book.setIsbn(bookDetails.getIsbn());
            book.setCoverImage(bookDetails.getCoverImage());
            book.setDescription(bookDetails.getDescription());
            book.setPublishYear(bookDetails.getPublishYear());
            book.setTotalStock(bookDetails.getTotalStock());
            book.setStatus(bookDetails.getStatus());
            // 更新可借数量（如果总库存减少，需要确保可借数量不小于0）
            int newAvailableStock = Math.min(book.getAvailableStock() + (bookDetails.getTotalStock() - book.getTotalStock()), bookDetails.getTotalStock());
            book.setAvailableStock(Math.max(newAvailableStock, 0));
            book.setUpdatedAt(LocalDateTime.now());
            return bookRepository.save(book);
        } else {
            throw new IllegalArgumentException("图书不存在");
        }
    }

    @Override
    @Transactional
    public void deleteBook(Long bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            bookRepository.deleteById(bookId);
        } else {
            throw new IllegalArgumentException("图书不存在");
        }
    }

    @Override
    @Transactional
    public Book updateBookStatus(Long bookId, Integer status) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setStatus(status);
            book.setUpdatedAt(LocalDateTime.now());
            return bookRepository.save(book);
        } else {
            throw new IllegalArgumentException("图书不存在");
        }
    }

    @Override
    @Transactional
    public boolean decreaseStock(Long bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if (book.getAvailableStock() > 0) {
                book.setAvailableStock(book.getAvailableStock() - 1);
                book.setBorrowCount(book.getBorrowCount() + 1);
                bookRepository.save(book);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public void increaseStock(Long bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setAvailableStock(book.getAvailableStock() + 1);
            bookRepository.save(book);
        }
    }
}