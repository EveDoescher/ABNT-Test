package com.doescher.ABNT.Formatters.PreTextual;

import com.doescher.ABNT.Domain.Models.Document;
import com.doescher.ABNT.Engine.WordEngine;
import com.doescher.ABNT.Formatters.ComponentFormatter;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.time.LocalDate;

public class CoverFormatter implements ComponentFormatter {

    @Override
    public boolean shouldRender(Document data){
        return true;
    }

    @Override
    public void format(XWPFDocument doc, Document data, WordEngine engine){
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
