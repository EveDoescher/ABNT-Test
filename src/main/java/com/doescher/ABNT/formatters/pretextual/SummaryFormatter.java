package com.doescher.ABNT.formatters.pretextual;

import com.doescher.ABNT.models.entities.Document;
import com.doescher.ABNT.helpers.WordHelper;
import com.doescher.ABNT.formatters.ComponentFormatter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(6)
public class SummaryFormatter implements ComponentFormatter {

    @Override
    public boolean shouldRender(Document data){
        return data.getSections() != null && !data.getSections().isEmpty();
    }

    @Override
    public void format(XWPFDocument doc, Document data, WordHelper engine){
        doc.createParagraph().setPageBreak(true);
        String font = data.getFontType();

        engine.addTOC(doc, data.getFontType());

        engine.enforceUpdate(doc);
    }
}
