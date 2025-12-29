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

@Component
@Order(8)
@RequiredArgsConstructor
public class ForeignAbstractFormatter implements ComponentFormatter {

    private final WordHelper wordHelper;

    @Override
    public boolean shouldRender(Document data){
        return data.getForeignAbstractContent() != null && !data.getForeignAbstractContent().isEmpty();
    }

    @Override
    public void format(XWPFDocument doc, Document data){
        String font = data.getFontType().getFamilyName();
        doc.createParagraph().setPageBreak(true);

        wordHelper.addParagraph(doc, AbntConstants.LABEL_FOREIGN_ABSTRACT, true, ParagraphAlignment.CENTER, 0, font);
        wordHelper.breakLines(doc, 1);

        wordHelper.abstractText(doc, data.getForeignAbstractContent(), font);
        wordHelper.breakLines(doc, 1);

        if (data.getForeignAbstractKeywords() != null && !data.getForeignAbstractKeywords().isEmpty()){
            String keywords = AbntConstants.LABEL_FOREIGN_KEYWORDS + String.join("; ", data.getForeignAbstractKeywords()) + ".";
            wordHelper.abstractText(doc, keywords, font);
        }
    }
}
