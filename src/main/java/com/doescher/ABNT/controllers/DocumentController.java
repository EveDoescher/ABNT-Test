package com.doescher.ABNT.controllers;

import com.doescher.ABNT.models.dto.DocumentDTO;
import com.doescher.ABNT.models.entities.Document;
import com.doescher.ABNT.mappers.DocumentMapper;
import com.doescher.ABNT.services.DocumentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentMapper mapper;

    public DocumentController(DocumentService documentService, DocumentMapper mapper) {
        this.documentService = documentService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createDocument(@RequestBody DocumentDTO dto){
        Document document = mapper.toEntity(dto);

        Document savedDocument = documentService.save(document);

        Map<String, Object> response = new HashMap<>();
        response.put("id", savedDocument.getId());
        response.put("message", "Documento criado com sucesso");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id){
        try{
            byte[] wordContent = documentService.generateWordDocument(id);

            String filename = "Trabalho_ABNT.docx";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(wordContent);
        }catch (IOException e){
            return ResponseEntity.internalServerError().build();
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
}


