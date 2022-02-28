package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Subcategory;

import java.util.List;

public interface SubcategoryService {
    List<Subcategory> getAllSubcategories();

    Subcategory saveSubcategory(Subcategory subcategory);

    Subcategory getSubcategoryById(Long id);

    Subcategory updateSubcategory(Subcategory subcategory);

    void deleteSubcategoryById(Long id);
}
