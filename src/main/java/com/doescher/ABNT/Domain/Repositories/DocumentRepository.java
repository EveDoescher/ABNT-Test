package com.doescher.ABNT.Domain.Repositories;

import com.doescher.ABNT.Domain.Models.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
}
