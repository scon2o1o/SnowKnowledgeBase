package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.Document;
import net.knowledgebase.springboot.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService{

    private DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        super();
        this.documentRepository = documentRepository;
    }

    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public Document getDocumentById(Long id) {
        return documentRepository.findById(id).get();
    }

    @Override
    public Document updateDocument(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public void deleteDocumentById(Long id) {
        documentRepository.deleteById(id);
    }

    @Override
    public List<Document> findDocumentsByCategory(String category) {
        return documentRepository.findByCategory(category);
    }

    @Override
    public List<Document> findByInternalTrue(){
        return documentRepository.findByInternalTrue();
    }

    @Override
    public List<Document> findByInternalFalse(){
        return documentRepository.findByInternalFalse();
    }
}
