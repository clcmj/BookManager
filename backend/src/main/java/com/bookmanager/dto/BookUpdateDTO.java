package com.bookmanager.dto;

import lombok.Data;

@Data
public class BookUpdateDTO {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private Long categoryId;
    private String coverImage;
    private String description;
    private Integer publishYear;
    private Integer totalStock;
    private Integer status;
}