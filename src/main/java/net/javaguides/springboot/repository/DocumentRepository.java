package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long>{

}
