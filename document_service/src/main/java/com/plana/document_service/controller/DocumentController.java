package com.plana.document_service.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.plana.document_service.entity.DocumentEntity;
import com.plana.document_service.service.DocumentService;
import com.plana.document_service.service.IdExtractorService;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final IdExtractorService idExtractorService;

    public DocumentController(DocumentService documentService, IdExtractorService idExtractorService) {
        this.documentService = documentService;
        this.idExtractorService = idExtractorService;
    }

    @PostMapping("/upload")
    public DocumentEntity upload(@RequestParam String title,
                                 @RequestParam String documentNo,
                                 @RequestParam MultipartFile file) throws Exception {
        return documentService.upload(title, documentNo, file);
    }

    @GetMapping("/search")
    public List<DocumentEntity> search(@RequestParam String title) {
        return documentService.searchByTitle(title);
    }

    @GetMapping("/all")
    public List<DocumentEntity> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/{id}/extract-id")
    public String extractId(@PathVariable String id) throws Exception {
        DocumentEntity doc = documentService.get(id);
        return idExtractorService.extractId(Path.of(doc.getFilePath()));
    }
}

