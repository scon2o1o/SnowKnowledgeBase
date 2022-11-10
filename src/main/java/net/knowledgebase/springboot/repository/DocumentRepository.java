package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long>{

    @Query(
            value = "SELECT * FROM Document u WHERE u.category = ?",
            nativeQuery = true)
    List<Document> findByCategory(String category);
}
