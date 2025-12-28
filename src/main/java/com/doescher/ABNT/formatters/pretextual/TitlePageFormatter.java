package com.doescher.ABNT.formatters.pretextual;

import com.doescher.ABNT.models.entities.Document;
import com.doescher.ABNT.helpers.WordHelper;
import com.doescher.ABNT.formatters.ComponentFormatter;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Order(2)
public class TitlePageFormatter implements ComponentFormatter {

    @Override
    public boolean shouldRender(Document data){
        return true;
    }

    @Override
    public void format(XWPFDocument doc, Document data, WordHelper engine){
        String font = data.getFontType();
        doc.createParagraph().setPageBreak(true);

        //ELEMENTOS DA FOLHA DE ROSTO
        data.getAuthors().forEach(authors -> engine.addParagraph(doc, authors.toUpperCase(), false, ParagraphAlignment.CENTER, 0, font));

        engine.breakLines(doc, 8);

        engine.addParagraph(doc, data.getTitle().toUpperCase(), true, ParagraphAlignment.CENTER, 0, font);
        if (data.getSubtitle() != null){
            engine.addParagraph(doc, data.getSubtitle().toUpperCase(), false, ParagraphAlignment.CENTER, 0, font);
        }

        engine.breakLines(doc, 4);

        renderProjectNote(doc, data, font);

        engine.breakLines(doc, 8);

        engine.addParagraph(doc, data.getCity().toUpperCase(), false, ParagraphAlignment.CENTER, 0, font);
        engine.addParagraph(doc, String.valueOf(LocalDate.now().getYear()), false, ParagraphAlignment.CENTER, 0, font);

    }

    private void renderProjectNote(XWPFDocument doc, Document data, String font){
        //Nota da Folha de rosto
        XWPFParagraph noteParagraph = doc.createParagraph();
        noteParagraph.setAlignment(ParagraphAlignment.BOTH);
        noteParagraph.setIndentationLeft(4535); // 8cm
        noteParagraph.setSpacingBetween(1.0);

        XWPFRun noteRun = noteParagraph.createRun();
        noteRun.setFontFamily(font);
        noteRun.setFontSize(10);
        noteRun.setText(data.getProjectNote());

        doc.createParagraph().setSpacingBetween(1.0);

        //Nome do Orientador
        XWPFParagraph advParagraph = doc.createParagraph();
        advParagraph.setIndentationLeft(4535); //8cm
        advParagraph.setSpacingBetween(1.0);

        XWPFRun advRun = advParagraph.createRun();
        advRun.setFontFamily(font);
        advRun.setFontSize(10);
        advRun.setText("Orientador: " + data.getAdvisor());

    }
}
