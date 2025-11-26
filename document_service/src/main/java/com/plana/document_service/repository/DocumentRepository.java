package com.plana.document_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plana.document_service.entity.DocumentEntity;

import java.util.List;

public interface DocumentRepository extends JpaRepository<DocumentEntity, String> {

    List<DocumentEntity> findByTitleContainingIgnoreCase(String title);
}

