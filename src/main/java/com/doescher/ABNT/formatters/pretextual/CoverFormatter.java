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
@Order(1)
public class CoverFormatter implements ComponentFormatter {

    @Override
    public boolean shouldRender(Document data){
        return data.getTitle() != null;
    }

    @Override
    public void format(XWPFDocument doc, Document data, WordHelper engine){
        String font = data.getFontType();

        int textLines = 2;
        if (data.getCourse() != null) textLines++;
        if (data.getAuthors() != null) textLines += data.getAuthors().size();
        textLines += (int) Math.ceil(data.getTitle().length() / 65.0);
        if (data.getSubtitle() != null) textLines += (int) Math.ceil(data.getSubtitle().length() / 65.0);

        int gap = Math.max((30 - textLines) / 3,1);

        //ELEMENTOS DA CAPA
        engine.addParagraph(doc, data.getInstitution().toUpperCase(), true, ParagraphAlignment.CENTER, 0, font);
        if (data.getCourse() != null){
            engine.addParagraph(doc, data.getCourse().toUpperCase(), true, ParagraphAlignment.CENTER, 0, font);
        }

        engine.breakLines(doc, gap);

        data.getAuthors().forEach(a -> engine.addParagraph(doc, a.toUpperCase(), false, ParagraphAlignment.CENTER, 0, font));

        engine.breakLines(doc, gap);

        engine.addParagraph(doc, data.getTitle().toUpperCase(), true, ParagraphAlignment.CENTER, 0, font);
        if (data.getSubtitle() != null){
            engine.addParagraph(doc, data.getSubtitle().toUpperCase(), false, ParagraphAlignment.CENTER, 0, font);
        }

        engine.breakLines(doc, gap);

        engine.addParagraph(doc, data.getCity().toUpperCase(), false, ParagraphAlignment.CENTER, 0, font);
        engine.addParagraph(doc, String.valueOf(LocalDate.now().getYear()), false, ParagraphAlignment.CENTER, 0, font);
    }
}
