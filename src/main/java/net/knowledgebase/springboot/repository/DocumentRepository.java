package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long>{

}
