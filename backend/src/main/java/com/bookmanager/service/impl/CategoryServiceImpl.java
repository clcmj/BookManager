package com.bookmanager.service.impl;

import com.bookmanager.model.Category;
import com.bookmanager.repository.CategoryRepository;
import com.bookmanager.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category addCategory(Category category) {
        // 检查分类名称是否已存在
        List<Category> existingCategories = categoryRepository.findByParentId(category.getParentId());
        for (Category existingCategory : existingCategories) {
            if (existingCategory.getName().equals(category.getName())) {
                throw new IllegalArgumentException("同一父分类下分类名称不能重复");
            }
        }
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> findById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findByParentId(Long parentId) {
        return categoryRepository.findByParentId(parentId);
    }

    @Override
    public List<Category> getCategoryTree() {
        List<Category> allCategories = categoryRepository.findAll();
        Map<Long, List<Category>> categoryMap = new HashMap<>();
        List<Category> rootCategories = new ArrayList<>();

        // 按父分类ID分组
        for (Category category : allCategories) {
            Long parentId = category.getParentId();
            if (!categoryMap.containsKey(parentId)) {
                categoryMap.put(parentId, new ArrayList<>());
            }
            categoryMap.get(parentId).add(category);
        }

        // 递归构建树形结构
        buildCategoryTree(rootCategories, categoryMap, 0L);

        return rootCategories;
    }

    private void buildCategoryTree(List<Category> tree, Map<Long, List<Category>> categoryMap, Long parentId) {
        List<Category> children = categoryMap.get(parentId);
        if (children != null) {
            for (Category child : children) {
                tree.add(child);
                buildCategoryTree(child.getBooks(), categoryMap, child.getId());
            }
        }
    }

    @Override
    @Transactional
    public Category updateCategory(Long categoryId, Category categoryDetails) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            // 检查分类名称是否已存在（同一父分类下）
            List<Category> existingCategories = categoryRepository.findByParentId(category.getParentId());
            for (Category existingCategory : existingCategories) {
                if (existingCategory.getName().equals(categoryDetails.getName()) && !existingCategory.getId().equals(categoryId)) {
                    throw new IllegalArgumentException("同一父分类下分类名称不能重复");
                }
            }
            category.setName(categoryDetails.getName());
            category.setParentId(categoryDetails.getParentId());
            category.setDescription(categoryDetails.getDescription());
            category.setSortOrder(categoryDetails.getSortOrder());
            category.setStatus(categoryDetails.getStatus());
            category.setUpdatedAt(LocalDateTime.now());
            return categoryRepository.save(category);
        } else {
            throw new IllegalArgumentException("分类不存在");
        }
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            // 检查是否有子分类
            List<Category> children = categoryRepository.findByParentId(categoryId);
            if (!children.isEmpty()) {
                throw new IllegalArgumentException("该分类下有子分类，不能删除");
            }
            // 检查是否有图书
            Category category = optionalCategory.get();
            if (!category.getBooks().isEmpty()) {
                throw new IllegalArgumentException("该分类下有图书，不能删除");
            }
            categoryRepository.deleteById(categoryId);
        } else {
            throw new IllegalArgumentException("分类不存在");
        }
    }
}