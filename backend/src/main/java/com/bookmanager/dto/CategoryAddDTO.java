package com.bookmanager.dto;

import lombok.Data;

@Data
public class CategoryAddDTO {
    private String name;
    private Long parentId;
    private String description;
    private Integer sortOrder;
}