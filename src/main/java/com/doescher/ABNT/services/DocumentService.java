package com.doescher.ABNT.services;

import com.doescher.ABNT.exceptions.DocumentNotFoundException;
import com.doescher.ABNT.models.entities.Document;
import com.doescher.ABNT.models.repositories.DocumentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository repository;

    @Transactional
    public Document save(Document document){
        return repository.save(document);
    }

    @Transactional(readOnly = true)
    public Document findById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));
    }
}
