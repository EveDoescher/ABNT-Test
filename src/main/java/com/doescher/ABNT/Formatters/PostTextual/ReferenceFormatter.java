package com.doescher.ABNT.Formatters.PostTextual;

import com.doescher.ABNT.Domain.Models.Document;
import com.doescher.ABNT.Engine.WordEngine;
import com.doescher.ABNT.Formatters.ComponentFormatter;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ReferenceFormatter implements ComponentFormatter {

    @Override
    public boolean shouldRender(Document data) {
        return true;
    }

    @Override
    public void format(XWPFDocument doc, Document data, WordEngine engine){
        String font = data.getFontType();

        doc.createParagraph().setPageBreak(true);

        engine.addPostTextualTitle(doc, "REFERÊNCIAS", font);

        List<String> sortedRefs = data.getReferences();
        Collections.sort(sortedRefs);

        for (String ref : sortedRefs){
            engine.addReferenceText(doc, ref, font);
        }
    }
}
