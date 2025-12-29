package com.doescher.ABNT.formatters.pretextual;

import com.doescher.ABNT.constants.AbntConstants;
import com.doescher.ABNT.helpers.WordHelper;
import com.doescher.ABNT.models.entities.Document;
import com.doescher.ABNT.formatters.ComponentFormatter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Order(2)
@RequiredArgsConstructor
public class TitlePageFormatter implements ComponentFormatter {

    private final WordHelper wordHelper;

    @Override
    public boolean shouldRender(Document data){
        return true;
    }

    @Override
    public void format(XWPFDocument doc, Document data){
        String font = data.getFontType().getFamilyName();
        doc.createParagraph().setPageBreak(true);

        //ELEMENTOS DA FOLHA DE ROSTO
        data.getAuthors().forEach(authors -> wordHelper.addParagraph(doc, authors.toUpperCase(), false, ParagraphAlignment.CENTER, 0, font));

        wordHelper.breakLines(doc, 8);

        wordHelper.addParagraph(doc, data.getTitle().toUpperCase(), true, ParagraphAlignment.CENTER, 0, font);
        if (data.getSubtitle() != null){
            wordHelper.addParagraph(doc, data.getSubtitle().toUpperCase(), false, ParagraphAlignment.CENTER, 0, font);
        }

        wordHelper.breakLines(doc, 4);

        renderProjectNote(doc, data, font);

        wordHelper.breakLines(doc, 8);

        wordHelper.addParagraph(doc, data.getCity().toUpperCase(), false, ParagraphAlignment.CENTER, 0, font);
        wordHelper.addParagraph(doc, String.valueOf(LocalDate.now().getYear()), false, ParagraphAlignment.CENTER, 0, font);

    }

    private void renderProjectNote(XWPFDocument doc, Document data, String font){
        //Nota da Folha de rosto
        XWPFParagraph noteParagraph = doc.createParagraph();
        noteParagraph.setAlignment(ParagraphAlignment.BOTH);
        noteParagraph.setIndentationLeft(AbntConstants.PARAGRAPH_INDENTATION_LEFT); // 8cm
        noteParagraph.setSpacingBetween(AbntConstants.SIMPLE_LINE_SPACING);

        XWPFRun noteRun = noteParagraph.createRun();
        noteRun.setFontFamily(font);
        noteRun.setFontSize(AbntConstants.FONT_SIZE_SMALL);
        noteRun.setText(data.getProjectNote());

        doc.createParagraph().setSpacingBetween(AbntConstants.SIMPLE_LINE_SPACING);

        //Nome do Orientador
        XWPFParagraph advParagraph = doc.createParagraph();
        advParagraph.setIndentationLeft(AbntConstants.PARAGRAPH_INDENTATION_LEFT); //8cm
        advParagraph.setSpacingBetween(AbntConstants.SIMPLE_LINE_SPACING);

        XWPFRun advRun = advParagraph.createRun();
        advRun.setFontFamily(font);
        advRun.setFontSize(AbntConstants.FONT_SIZE_SMALL);
        advRun.setText(AbntConstants.LABEL_ADVISOR + data.getAdvisor());

    }
}
