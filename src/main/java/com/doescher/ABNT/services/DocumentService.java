package com.doescher.ABNT.services;

import com.doescher.ABNT.models.entities.Document;
import com.doescher.ABNT.models.repositories.DocumentRepository;
import com.doescher.ABNT.helpers.WordHelper;
import com.doescher.ABNT.formatters.ComponentFormatter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository repository;
    private final WordHelper wordHelper;
    private final List<ComponentFormatter> formatters;

    public DocumentService(DocumentRepository repository, WordHelper wordHelper, List<ComponentFormatter> formatters) {
        this.repository = repository;
        this.wordHelper = wordHelper;
        this.formatters = formatters;
    }

    public Document save(Document document){
        return repository.save(document);
    }

    public byte[] generateWordDocument(Long id) throws IOException{
        Document document = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não foi encontrado documento com id: " + id));

        try (InputStream templateStream = getClass().getResourceAsStream("/templates/template.docx");
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            XWPFDocument wordDoc;

            if (templateStream != null) {
                wordDoc = new XWPFDocument(templateStream);
            } else {
                System.out.println("Template não encontrado! Criando documento em branco.");
                wordDoc = new XWPFDocument();
                wordHelper.setupAbntPage(wordDoc);
            }


            try (wordDoc) {
                for (ComponentFormatter formatter : formatters) {
                    if (formatter.shouldRender(document)) {
                        formatter.format(wordDoc, document, wordHelper);
                    }
                }

                wordDoc.write(out);
            }

            return out.toByteArray();
        }
    }

    public Document findById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não foi encontrado documento com id: " + id));
    }
}
