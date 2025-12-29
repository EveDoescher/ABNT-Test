package com.doescher.ABNT.services;

import com.doescher.ABNT.formatters.ComponentFormatter;
import com.doescher.ABNT.helpers.WordHelper;
import com.doescher.ABNT.models.entities.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentExportService {

    private final DocumentService documentService;
    private final List<ComponentFormatter> formatters;
    private final WordHelper wordHelper;

    @Transactional(readOnly = true)
    public byte[] generateAbntWordDocument(Long id){
        Document document = documentService.findById(id);

        try (InputStream templateStream = getClass().getResourceAsStream("/templates/template.docx");
                ByteArrayOutputStream out =  new ByteArrayOutputStream()){

            XWPFDocument wordDoc;

            if (templateStream != null){
                log.info("Template encontrado");
                wordDoc = new XWPFDocument(templateStream);
            } else {
                log.warn("Template não encontrado. Criando documento em branco com setup manual");
                wordDoc = new XWPFDocument();
                wordHelper.setupAbntPage(wordDoc);
            }

            try (wordDoc){
                for (ComponentFormatter formatter : formatters){
                    if (formatter.shouldRender(document)){
                        formatter.format(wordDoc, document);
                    }
                }
                wordDoc.write(out);
            }

            return out.toByteArray();

        }catch (IOException e){
            log.error("Erro ao gerar documento Word", e);
            throw new RuntimeException("Falha ao gerar arquivo do documento", e);
        }
    }
}
