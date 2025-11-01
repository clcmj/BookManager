package com.bookmanager.service;

import com.bookmanager.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category addCategory(Category category);
    Optional<Category> findById(Long categoryId);
    List<Category> findAll();
    List<Category> findByParentId(Long parentId);
    List<Category> getCategoryTree();
    Category updateCategory(Long categoryId, Category categoryDetails);
    void deleteCategory(Long categoryId);
}