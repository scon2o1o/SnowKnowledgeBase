package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    Category saveCategory(Category category);

    Category getCategoryById(Long id);

    Category updateCategory(Category category);

    void deleteCategoryById(Long id);
}
