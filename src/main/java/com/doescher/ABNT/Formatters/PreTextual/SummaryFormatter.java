package com.doescher.ABNT.Formatters.PreTextual;

import com.doescher.ABNT.Domain.Models.Document;
import com.doescher.ABNT.Engine.WordEngine;
import com.doescher.ABNT.Formatters.ComponentFormatter;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
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
    public void format(XWPFDocument doc, Document data, WordEngine engine){
        doc.createParagraph().setPageBreak(true);
        String font = data.getFontType();

        engine.addTOC(doc, data.getFontType());

        engine.enforceUpdate(doc);
    }
}
