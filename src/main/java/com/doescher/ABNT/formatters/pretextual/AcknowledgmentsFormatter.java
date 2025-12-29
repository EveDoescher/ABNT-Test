package com.doescher.ABNT.formatters.pretextual;

import com.doescher.ABNT.constants.AbntConstants;
import com.doescher.ABNT.formatters.ComponentFormatter;
import com.doescher.ABNT.helpers.WordHelper;
import com.doescher.ABNT.models.entities.Document;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(5)
@RequiredArgsConstructor
public class AcknowledgmentsFormatter implements ComponentFormatter {
    private final WordHelper wordHelper;

    @Override
    public boolean shouldRender(Document data){
        return data.getAcknowledgment() != null && !data.getAcknowledgment().isEmpty();
    }

    @Override
    public void format(XWPFDocument doc, Document data){
        String font = data.getFontType().getFamilyName();

        doc.createParagraph().setPageBreak(true);
        wordHelper.addParagraph(doc, AbntConstants.LABEL_ACKNOWLEDGMENT, true, ParagraphAlignment.CENTER, 0,font);
        wordHelper.breakLines(doc, 1);
        wordHelper.abstractText(doc, data.getAcknowledgment(), font);

    }
}
