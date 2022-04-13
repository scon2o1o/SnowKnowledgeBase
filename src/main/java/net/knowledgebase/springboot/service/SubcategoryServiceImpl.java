package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.Subcategory;
import net.knowledgebase.springboot.repository.SubcategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubcategoryServiceImpl implements SubcategoryService{

    private SubcategoryRepository subcategoryRepo;

    public SubcategoryServiceImpl(SubcategoryRepository subcategoryRepo) {
        super();
        this.subcategoryRepo = subcategoryRepo;
    }

    @Override
    public List<Subcategory> getAllSubcategories() {
        return subcategoryRepo.findAll();
    }

    @Override
    public Subcategory saveSubcategory(Subcategory subcategory) {
        return subcategoryRepo.save(subcategory);
    }

    @Override
    public Subcategory getSubcategoryById(Long id) {
        return subcategoryRepo.findById(id).get();
    }

    @Override
    public Subcategory updateSubcategory(Subcategory subcategory) {
        return subcategoryRepo.save(subcategory);
    }

    @Override
    public void deleteSubcategoryById(Long id) {
        subcategoryRepo.deleteById(id);
    }
}
