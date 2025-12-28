package com.doescher.ABNT.formatters.pretextual;

import com.doescher.ABNT.models.entities.Document;
import com.doescher.ABNT.helpers.WordHelper;
import com.doescher.ABNT.formatters.ComponentFormatter;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
public class AbstractFormatter implements ComponentFormatter {

    @Override
    public boolean shouldRender(Document data){
        return data.getAbstractContent() != null && !data.getAbstractContent().isEmpty();
    }

    @Override
    public void format(XWPFDocument doc, Document data, WordHelper engine){
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
