package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
}
