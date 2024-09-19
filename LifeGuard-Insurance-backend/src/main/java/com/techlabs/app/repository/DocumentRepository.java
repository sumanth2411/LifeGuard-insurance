package com.techlabs.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.app.entity.Document;
import com.techlabs.app.entity.DocumentType;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.SubmittedDocument;

public interface DocumentRepository extends JpaRepository<SubmittedDocument, Long> {

	//Page<Document> findByDocumentName(DocumentType documentType, PageRequest pageable);

	

}
