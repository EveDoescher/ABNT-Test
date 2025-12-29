package com.doescher.ABNT.formatters.posttextual;

import com.doescher.ABNT.constants.AbntConstants;
import com.doescher.ABNT.helpers.WordHelper;
import com.doescher.ABNT.models.entities.Document;
import com.doescher.ABNT.formatters.ComponentFormatter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Order(11)
@RequiredArgsConstructor
public class ReferenceFormatter implements ComponentFormatter {

    private final WordHelper wordHelper;

    @Override
    public boolean shouldRender(Document data) {
        return data.getReferences() != null && !data.getReferences().isEmpty();
    }

    @Override
    public void format(XWPFDocument doc, Document data){
        String font = data.getFontType().getFamilyName();

        doc.createParagraph().setPageBreak(true);

        wordHelper.addPostTextualTitle(doc, AbntConstants.LABEL_REFERENCES, font);

        List<String> sortedRefs = data.getReferences();
        Collections.sort(sortedRefs);

        for (String ref : sortedRefs){
            wordHelper.addReferenceText(doc, ref, font);
        }
    }
}
