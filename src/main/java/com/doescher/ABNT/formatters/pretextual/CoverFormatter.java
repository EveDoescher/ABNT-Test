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
@Order(1)
@RequiredArgsConstructor
public class CoverFormatter implements ComponentFormatter {

    private final WordHelper wordHelper;

    @Override
    public boolean shouldRender(Document data){
        return data.getTitle() != null;
    }

    @Override
    public void format(XWPFDocument doc, Document data){
        String font = data.getFontType().getFamilyName();

        int textLines = 2;
        if (data.getCourse() != null) textLines++;
        if (data.getAuthors() != null) textLines += data.getAuthors().size();
        textLines += (int) Math.ceil(data.getTitle().length() / 65.0);
        if (data.getSubtitle() != null) textLines += (int) Math.ceil(data.getSubtitle().length() / 65.0);

        int gap = Math.max((30 - textLines) / 3,1);

        //ELEMENTOS DA CAPA
        wordHelper.addParagraph(doc, data.getInstitution().toUpperCase(), true, ParagraphAlignment.CENTER, 0, font);
        if (data.getCourse() != null){
            wordHelper.addParagraph(doc, data.getCourse().toUpperCase(), true, ParagraphAlignment.CENTER, 0, font);
        }

        wordHelper.breakLines(doc, gap);

        data.getAuthors().forEach(a -> wordHelper.addParagraph(doc, a.toUpperCase(), false, ParagraphAlignment.CENTER, 0, font));

        wordHelper.breakLines(doc, gap);

        wordHelper.addParagraph(doc, data.getTitle().toUpperCase(), true, ParagraphAlignment.CENTER, 0, font);
        if (data.getSubtitle() != null){
            wordHelper.addParagraph(doc, data.getSubtitle().toUpperCase(), false, ParagraphAlignment.CENTER, 0, font);
        }

        wordHelper.breakLines(doc, gap);

        wordHelper.addParagraph(doc, data.getCity().toUpperCase(), false, ParagraphAlignment.CENTER, 0, font);
        wordHelper.addParagraph(doc, String.valueOf(LocalDate.now().getYear()), false, ParagraphAlignment.CENTER, 0, font);
    }
}
