package com.doescher.ABNT.formatters.pretextual;

import com.doescher.ABNT.constants.AbntConstants;
import com.doescher.ABNT.helpers.WordHelper;
import com.doescher.ABNT.models.entities.Document;
import com.doescher.ABNT.formatters.ComponentFormatter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Order(3)
@RequiredArgsConstructor
public class ErrataFormatter implements ComponentFormatter {

    private final WordHelper wordHelper;

    @Override
    public boolean shouldRender(Document data){
        return data.getErrata() != null && !data.getErrata().isEmpty();
    }

    @Override
    public void format(XWPFDocument doc, Document data){
        String font = data.getFontType().getFamilyName();
        doc.createParagraph().setPageBreak(true);

        wordHelper.addParagraph(doc, AbntConstants.LABEL_ERRATA, true, ParagraphAlignment.CENTER, 0, font);
        wordHelper.breakLines(doc, 2);

        String reference = data.getAuthors().get(0).toUpperCase() + ". " + data.getTitle() + ". " + String.valueOf(LocalDate.now().getYear() + ".");

        wordHelper.addBodyText(doc, reference, font);
        wordHelper.breakLines(doc, 2);

        wordHelper.addErrataTable(doc, data.getErrata(), font);

    }
}
