package com.bookmanager.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(nullable = false, length = 100)
    private String author;
    
    @Column(length = 100)
    private String publisher;
    
    @Column(unique = true, nullable = false, length = 50)
    private String isbn;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @Column(name = "cover_image", length = 255)
    private String coverImage;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "publish_year")
    private Integer publishYear;
    
    @Column(name = "total_stock", nullable = false)
    private Integer totalStock = 0;
    
    @Column(name = "available_stock", nullable = false)
    private Integer availableStock = 0;
    
    @Column(name = "borrow_count", nullable = false)
    private Integer borrowCount = 0;
    
    @Column(nullable = false)
    private Integer status = 1;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BorrowRecord> borrowRecords;
}