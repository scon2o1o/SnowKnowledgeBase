package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    Category saveCategory(Category category);

    Category getCategoryById(Long id);

    Category updateCategory(Category category);

    void deleteCategoryById(Long id);
}
