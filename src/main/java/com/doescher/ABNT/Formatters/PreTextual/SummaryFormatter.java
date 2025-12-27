package com.doescher.ABNT.Formatters.PreTextual;

import com.doescher.ABNT.Domain.Models.Document;
import com.doescher.ABNT.Engine.WordEngine;
import com.doescher.ABNT.Formatters.ComponentFormatter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class SummaryFormatter implements ComponentFormatter {

    @Override
    public boolean shouldRender(Document data){
        return true;
    }

    @Override
    public void format(XWPFDocument doc, Document data, WordEngine engine){
        doc.createParagraph().setPageBreak(true);

        engine.addTOC(doc, data.getFontType());

        engine.enforceUpdate(doc);
    }
}
