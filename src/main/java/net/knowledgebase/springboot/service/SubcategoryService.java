package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.Subcategory;

import java.util.List;

public interface SubcategoryService {
    List<Subcategory> getAllSubcategories();

    Subcategory saveSubcategory(Subcategory subcategory);

    Subcategory getSubcategoryById(Long id);

    Subcategory updateSubcategory(Subcategory subcategory);

    void deleteSubcategoryById(Long id);
}
