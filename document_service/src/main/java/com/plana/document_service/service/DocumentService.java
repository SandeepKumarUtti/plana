package com.plana.document_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.plana.document_service.entity.DocumentEntity;
import com.plana.document_service.repository.DocumentRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class DocumentService {

    @Value("${storage.path}")
    private String storagePath;

    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    public DocumentEntity upload(String title, String documentNo, MultipartFile file) throws Exception {
        Path folder = Path.of(storagePath);
        if (!Files.exists(folder)) Files.createDirectories(folder);

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path destination = folder.resolve(fileName);

        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        DocumentEntity doc = new DocumentEntity();
        doc.setTitle(title);
        doc.setDocumentNo(documentNo);
        doc.setFileName(fileName);
        doc.setFilePath(destination.toString());
        return repository.save(doc);
    }

    public List<DocumentEntity> searchByTitle(String title) {
        return repository.findByTitleContainingIgnoreCase(title);
    }

    public DocumentEntity get(String id) {
        return repository.findById(id).orElseThrow();
    }
    public List<DocumentEntity> getAllDocuments() {
        return repository.findAll();
    }
}

