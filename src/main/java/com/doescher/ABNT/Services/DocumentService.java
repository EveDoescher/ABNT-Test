package com.doescher.ABNT.Services;

import com.doescher.ABNT.Domain.Models.Document;
import com.doescher.ABNT.Domain.Repositories.DocumentRepository;
import com.doescher.ABNT.Engine.WordEngine;
import com.doescher.ABNT.Formatters.ComponentFormatter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository repository;
    private final WordEngine wordEngine;
    private final List<ComponentFormatter> formatters;

    public DocumentService(DocumentRepository repository, WordEngine wordEngine, List<ComponentFormatter> formatters) {
        this.repository = repository;
        this.wordEngine = wordEngine;
        this.formatters = formatters;
    }

    public Document save(Document document){
        return repository.save(document);
    }

    public byte[] generateWordDocument(Long id) throws IOException{
        Document document = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não foi encontrado documento com id: " + id));

        try (XWPFDocument wordDoc = new XWPFDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()){
            for (ComponentFormatter formatter : formatters){
                if (formatter.shouldRender(document)){
                    formatter.format(wordDoc, document, wordEngine);
                }
            }

            wordDoc.write(out);
            return out.toByteArray();
        }
    }

    public Document findById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não foi encontrado documento com id: " + id));
    }
}
