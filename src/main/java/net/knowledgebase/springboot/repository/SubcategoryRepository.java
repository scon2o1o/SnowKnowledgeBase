package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
}
