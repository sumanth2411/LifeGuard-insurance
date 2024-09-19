package com.techlabs.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Document;
import com.techlabs.app.entity.DocumentType;
import com.techlabs.app.entity.Role;

@Repository
public interface DocumentRepository1 extends JpaRepository<Document, Integer> {
	Optional<Document> findById(int documentId);

	Page<Document> findByDocumentName(DocumentType documentType, PageRequest pageable);

}
