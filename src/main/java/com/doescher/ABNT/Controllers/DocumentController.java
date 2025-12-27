package com.doescher.ABNT.Controllers;

import com.doescher.ABNT.Domain.DTO.DocumentDTO;
import com.doescher.ABNT.Domain.DTO.SectionDTO;
import com.doescher.ABNT.Domain.Models.Document;
import com.doescher.ABNT.Domain.Models.ErrataItem;
import com.doescher.ABNT.Domain.Models.Section;
import com.doescher.ABNT.Domain.Repositories.DocumentRepository;
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
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentRepository repository;
    private final DocumentService documentService;

    @PostMapping
    @Transactional
    public ResponseEntity<Map<String, Object>> create(@RequestBody DocumentDTO dto) {
        Document doc = new Document();

        doc.setFontType(dto.fontType() != null ? dto.fontType() : "Arial");

        //CAPA
        doc.setInstitution(dto.cover().institution());
        doc.setCourse(dto.cover().course());
        doc.setTitle(dto.cover().title());
        doc.setSubtitle(dto.cover().subtitle());
        doc.setCity(dto.cover().city());
        doc.setAuthors(dto.cover().authors());

        //FOLHA ROSTO
        doc.setProjectNote(dto.titlePage().projectNote());
        doc.setAdvisor(dto.titlePage().advisor());

        //ERRATA
        if (dto.errata() != null && !dto.errata().isEmpty()) {
            List<ErrataItem> errataList = dto.errata().stream().map(itemDto -> {
                ErrataItem item = new ErrataItem();
                item.setPage(itemDto.page());
                item.setLine(itemDto.line());
                item.setTextFrom(itemDto.textFrom());
                item.setTextTo(itemDto.textTo());
                item.setDocument(doc);
                return item;
            }).toList();
            doc.setErrata(errataList);
        }

        //RESUMO PTBR
        doc.setAbstractContent(dto.nativeAbstract().content());
        doc.setAbstractKeywords(dto.nativeAbstract().keywords());

        //RESUMO ENG
        doc.setForeignAbstractContent(dto.foreignAbstract().content());
        doc.setForeignAbstractKeywords(dto.foreignAbstract().keywords());

        //SEÇÕES
        dto.sections().forEach(sectionDTO -> {
            saveSectionRecursive(sectionDTO, null, doc);
        });

        //REFERENCIAS
        doc.setReferences(dto.references().items());

        Document savedDoc = repository.save(doc);

        Map<String, Object> response = new HashMap<>();
        response.put("id", savedDoc.getId());
        response.put("message", "Documento criado com sucesso.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/export/{id}")
    public ResponseEntity<byte[]> exportToWord(@PathVariable Long id) throws IOException{
        Document document = repository.findById(id).orElseThrow(() -> new RuntimeException("Documento não encontrado"));

        byte[] wordFile = documentService.generateWord(document);

        String filename = "Trabalho_ABNT.docx";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(wordFile);
    }

    private void saveSectionRecursive(SectionDTO dto, Section parent, Document doc){
        Section section = new Section();
        section.setTitle(dto.title());
        section.setContent(dto.content());
        section.setSectionOrder(dto.sectionOrder());
        section.setParent(parent);

        doc.addSection(section);

        if (dto.subSections() != null && !dto.subSections().isEmpty()){
            dto.subSections().forEach(subDto -> {
                saveSectionRecursive(subDto, section, doc);
            });
        }
    }
}
