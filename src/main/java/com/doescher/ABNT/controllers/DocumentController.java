package com.doescher.ABNT.controllers;

import com.doescher.ABNT.models.dto.request.DocumentRequest;
import com.doescher.ABNT.models.dto.response.DocumentDetailResponse;
import com.doescher.ABNT.models.dto.response.DocumentResponse;
import com.doescher.ABNT.models.entities.Document;
import com.doescher.ABNT.mappers.DocumentMapper;
import com.doescher.ABNT.services.DocumentExportService;
import com.doescher.ABNT.services.DocumentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentExportService documentExportService;
    private final DocumentMapper mapper;

    public DocumentController(DocumentService documentService, DocumentMapper mapper, DocumentExportService documentExportService) {
        this.documentService = documentService;
        this.mapper = mapper;
        this.documentExportService = documentExportService;
    }

    @PostMapping
    public ResponseEntity<DocumentResponse> createDocument(@RequestBody DocumentRequest request){
        Document document = mapper.toEntity(request);

        Document savedDocument = documentService.save(document);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(savedDocument));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDetailResponse> findById(@PathVariable Long id) {
        Document document = documentService.findById(id);
        return ResponseEntity.ok(mapper.toDetailResponse(document));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id){
            byte[] wordContent = documentExportService.generateAbntWordDocument(id);

            String filename = "Trabalho_ABNT.docx";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(wordContent);
    }
}


