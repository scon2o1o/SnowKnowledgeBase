package net.javaguides.springboot.service;


import net.javaguides.springboot.model.Document;

import java.util.List;

public interface DocumentService {
    List<Document> getAllDocuments();

    Document saveDocument(Document document);

    Document getDocumentById(Long id);

    Document updateDocument(Document document);

    void deleteDocumentById(Long id);
}
