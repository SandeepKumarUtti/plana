package com.plana.document_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;

    private String documentNo;

    private String fileName;

    private String filePath;

    private LocalDateTime uploadedAt = LocalDateTime.now();

    // getters and setters
}

