package com.doescher.ABNT.formatters.posttextual;

import com.doescher.ABNT.models.entities.Document;
import com.doescher.ABNT.helpers.WordHelper;
import com.doescher.ABNT.formatters.ComponentFormatter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Order(8)
public class ReferenceFormatter implements ComponentFormatter {

    @Override
    public boolean shouldRender(Document data) {
        return data.getReferences() != null && !data.getReferences().isEmpty();
    }

    @Override
    public void format(XWPFDocument doc, Document data, WordHelper engine){
        String font = data.getFontType();

        doc.createParagraph().setPageBreak(true);

        engine.addPostTextualTitle(doc, "REFERÊNCIAS", font);

        List<String> sortedRefs = data.getReferences();
        Collections.sort(sortedRefs);

        for (String ref : sortedRefs){
            engine.addReferenceText(doc, ref, font);
        }
    }
}
