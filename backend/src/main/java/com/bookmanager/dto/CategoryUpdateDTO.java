package com.bookmanager.dto;

import lombok.Data;

@Data
public class CategoryUpdateDTO {
    private Long id;
    private String name;
    private Long parentId;
    private String description;
    private Integer sortOrder;
    private Integer status;
}