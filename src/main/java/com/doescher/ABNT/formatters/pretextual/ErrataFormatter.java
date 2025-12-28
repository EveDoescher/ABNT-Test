package com.doescher.ABNT.formatters.pretextual;

import com.doescher.ABNT.models.entities.Document;
import com.doescher.ABNT.helpers.WordHelper;
import com.doescher.ABNT.formatters.ComponentFormatter;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Order(3)
public class ErrataFormatter implements ComponentFormatter {

    @Override
    public boolean shouldRender(Document data){
        return data.getErrata() != null && !data.getErrata().isEmpty();
    }

    @Override
    public void format(XWPFDocument doc, Document data, WordHelper engine){
        String font = data.getFontType();
        doc.createParagraph().setPageBreak(true);

        engine.addParagraph(doc, "ERRATA", true, ParagraphAlignment.CENTER, 0, font);
        engine.breakLines(doc, 2);

        String reference = data.getAuthors().get(0).toUpperCase() + ". " + data.getTitle() + ". " + String.valueOf(LocalDate.now().getYear() + ".");

        engine.addBodyText(doc, reference, font);
        engine.breakLines(doc, 2);

        engine.addErrataTable(doc, data.getErrata(), font);

    }
}
