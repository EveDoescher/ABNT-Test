package com.doescher.ABNT.formatters.pretextual;

import com.doescher.ABNT.constants.AbntConstants;
import com.doescher.ABNT.formatters.ComponentFormatter;
import com.doescher.ABNT.helpers.WordHelper;
import com.doescher.ABNT.models.entities.Document;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(6)
@RequiredArgsConstructor
public class EpigraphFormatter implements ComponentFormatter {
    private final WordHelper wordHelper;

    @Override
    public boolean shouldRender(Document data) { return data.getEpigraph() != null && !data.getEpigraph().isEmpty(); }

    @Override
    public void format(XWPFDocument doc, Document data) {
        String font = data.getFontType().getFamilyName();

        doc.createParagraph().setPageBreak(true);
        wordHelper.breakLines(doc, 20);
        wordHelper.addRightAlignedText(doc, data.getEpigraph(), font, AbntConstants.PARAGRAPH_INDENTATION_LEFT_2, true);

    }
}
