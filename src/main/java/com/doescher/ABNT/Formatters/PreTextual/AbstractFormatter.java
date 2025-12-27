package com.doescher.ABNT.Formatters.PreTextual;

import com.doescher.ABNT.Domain.Models.Document;
import com.doescher.ABNT.Engine.WordEngine;
import com.doescher.ABNT.Formatters.ComponentFormatter;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class AbstractFormatter implements ComponentFormatter {

    @Override
    public boolean shouldRender(Document data){
        return true;
    }

    @Override
    public void format(XWPFDocument doc, Document data, WordEngine engine){
        String font = data.getFontType();
        doc.createParagraph().setPageBreak(true);

        engine.addParagraph(doc, "RESUMO", true, ParagraphAlignment.CENTER, 0, font);
        engine.breakLines(doc, 1);

        engine.abstractText(doc, data.getAbstractContent(), font);
        engine.breakLines(doc, 1);

        if (data.getAbstractKeywords() != null && !data.getAbstractKeywords().isEmpty()){
            String keywords = "Palavras-chave: " + String.join(". ", data.getAbstractKeywords()) + ".";
            engine.abstractText(doc, keywords, font);
        }
    }
}
