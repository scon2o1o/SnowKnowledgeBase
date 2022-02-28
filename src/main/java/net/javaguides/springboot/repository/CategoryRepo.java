package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
}
