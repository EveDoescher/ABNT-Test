package com.doescher.ABNT.Controllers;

import com.doescher.ABNT.Domain.DTO.DocumentDTO;
import com.doescher.ABNT.Domain.DTO.SectionDTO;
import com.doescher.ABNT.Domain.Models.Document;
import com.doescher.ABNT.Domain.Models.ErrataItem;
import com.doescher.ABNT.Domain.Models.Section;
import com.doescher.ABNT.Domain.Repositories.DocumentRepository;
import com.doescher.ABNT.Mappers.DocumentMapper;
import com.doescher.ABNT.Services.DocumentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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


