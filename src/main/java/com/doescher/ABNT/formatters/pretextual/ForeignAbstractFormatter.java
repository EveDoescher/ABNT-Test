package com.doescher.ABNT.formatters.pretextual;

import com.doescher.ABNT.models.entities.Document;
import com.doescher.ABNT.helpers.WordHelper;
import com.doescher.ABNT.formatters.ComponentFormatter;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(5)
public class ForeignAbstractFormatter implements ComponentFormatter {

    @Override
    public boolean shouldRender(Document data){
        return data.getForeignAbstractContent() != null && !data.getForeignAbstractContent().isEmpty();
    }

    @Override
    public void format(XWPFDocument doc, Document data, WordHelper engine){
        String font = data.getFontType();
        doc.createParagraph().setPageBreak(true);

        engine.addParagraph(doc, "ABSTRACT", true, ParagraphAlignment.CENTER, 0, font);
        engine.breakLines(doc, 1);

        engine.abstractText(doc, data.getForeignAbstractContent(), font);
        engine.breakLines(doc, 1);

        if (data.getForeignAbstractKeywords() != null && !data.getForeignAbstractKeywords().isEmpty()){
            String keywords = "Keywords: " + String.join(". ", data.getForeignAbstractKeywords()) + ".";
            engine.abstractText(doc, keywords, font);
        }
    }
}
