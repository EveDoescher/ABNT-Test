package com.doescher.ABNT.Services;

import com.doescher.ABNT.Domain.Models.Document;
import com.doescher.ABNT.Engine.WordEngine;
import com.doescher.ABNT.Formatters.ComponentFormatter;
import com.doescher.ABNT.Formatters.PostTextual.ReferenceFormatter;
import com.doescher.ABNT.Formatters.PreTextual.*;
import com.doescher.ABNT.Formatters.Textual.SectionFormatter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final WordEngine engine;

    private final List<ComponentFormatter> pipeline = List.of(
            new CoverFormatter(),
            new TitlePageFormatter(),
            new ErrataFormatter(),
            new AbstractFormatter(),
            new ForeignAbstractFormatter(),
            new SummaryFormatter(),
            new SectionFormatter(),
            new ReferenceFormatter()
    );

    public byte[] generateWord(Document document) throws IOException{
        InputStream inputStream = getClass().getResourceAsStream("/templates/template.docx");

        XWPFDocument doc;
        if (inputStream != null){
            doc = new XWPFDocument(inputStream);
        }else {
            doc = new XWPFDocument();
        }

        try (doc){
            for (ComponentFormatter formatter : pipeline){
                if (formatter.shouldRender(document)){
                    formatter.format(doc, document, engine);
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.write(out);
            return out.toByteArray();
        }
    }
}
