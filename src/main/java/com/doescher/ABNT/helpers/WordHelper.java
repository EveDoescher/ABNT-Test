package com.doescher.ABNT.helpers;

import com.doescher.ABNT.constants.AbntConstants;
import com.doescher.ABNT.models.entities.ErrataItem;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class WordHelper {

    //Metodo para adicionar paragrafo formatado
    public void addParagraph(XWPFDocument doc, String text, boolean bold, ParagraphAlignment align, int spacingAfter, String fontFamily){
        if (text == null) return;

        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.setAlignment(align);
        paragraph.setSpacingAfter(spacingAfter);
        paragraph.setSpacingBefore(AbntConstants.NO_SPACING);
        paragraph.setSpacingBetween(AbntConstants.LINE_SPACING, LineSpacingRule.AUTO);

        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setBold(bold);
        run.setFontFamily(fontFamily);
        run.setFontSize(AbntConstants.FONT_SIZE_TEXT);
    }

    //Metodo para pular linhas
    public void breakLines(XWPFDocument doc, int qnt){
        for (int i = 0; i < qnt; i++){
            XWPFParagraph paragraph = doc.createParagraph();
            paragraph.setSpacingAfter(AbntConstants.NO_SPACING);
            paragraph.setSpacingBefore(AbntConstants.NO_SPACING);
            paragraph.setSpacingBetween(AbntConstants.LINE_SPACING, LineSpacingRule.AUTO);
        }
    }

    //Metodo para adicionar titulos nivel 1 com quebra de pagina
    public void applyHeading(XWPFDocument doc, int level, String text, String fontFamily){
        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.setStyle("Heading" + Math.min(level, 5));

        if (level == 1){
            paragraph.setPageBreak(true);
        }

        XWPFRun run = paragraph.createRun();
        run.setFontFamily(fontFamily);
        run.setText(text);
    }

    //Metodo para adicionar/formatar texto do corpo da seção
    public void addBodyText(XWPFDocument doc, String content, String fontFamily){
        if (content == null) return;

        for (String line : content.split("\n")){
            XWPFParagraph paragraph = doc.createParagraph();
            paragraph.setAlignment(ParagraphAlignment.BOTH);
            paragraph.setSpacingAfter(AbntConstants.NO_SPACING);
            paragraph.setSpacingBetween(AbntConstants.LINE_SPACING, LineSpacingRule.AUTO);
            paragraph.setIndentationFirstLine(AbntConstants.INDENTATION_FIRST_LINE); //1.25cm

            XWPFRun run = paragraph.createRun();
            run.setText(line.trim());
            run.setFontFamily(fontFamily);

        }
    }

    //Metodo para configurar o texto do resumo
    public void abstractText(XWPFDocument doc, String content, String fontFamily){
        if (content == null) return;

        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setSpacingBetween(AbntConstants.LINE_SPACING, LineSpacingRule.AUTO);
        paragraph.setIndentationFirstLine(0);

        XWPFRun run = paragraph.createRun();
        run.setText(content);
        run.setFontFamily(fontFamily);
        run.setFontSize(AbntConstants.FONT_SIZE_TEXT);
    }

    //Metodo para configurar a criação do sumario
    public void addTOC(XWPFDocument doc, String fontFamily){
        addParagraph(doc, AbntConstants.LABEL_SUMMARY, true, ParagraphAlignment.CENTER, 0, fontFamily);
        breakLines(doc, 1);

        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun runBegin = paragraph.createRun();
        runBegin.getCTR().addNewFldChar().setFldCharType(STFldCharType.BEGIN);


        XWPFRun runInstr = paragraph.createRun();
        CTText text = runInstr.getCTR().addNewInstrText();
        text.setStringValue("TOC \\o \"1-3\" \\h \\z \\u");

        XWPFRun runSep = paragraph.createRun();
        runSep.getCTR().addNewFldChar().setFldCharType(STFldCharType.SEPARATE);

        XWPFRun runEnd = paragraph.createRun();
        runEnd.getCTR().addNewFldChar().setFldCharType(STFldCharType.END);
    }

    //Metodo para forçar update
    public void enforceUpdate(XWPFDocument doc){
        try{
            doc.enforceUpdateFields();
        } catch (Exception e){
            System.out.println("Não foi possivel forçar UpdateFields");
        }

    }

    //Metodo para adicionar/formatar textos com recuo a esquerda
    public void addRightAlignedText(XWPFDocument doc, String text, String fontFamily,int identation, boolean italic) {
        if (text == null || text.isEmpty()) return;

        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.BOTH);

        paragraph.setIndentationLeft(identation);

        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setFontFamily(fontFamily);
        run.setFontSize(AbntConstants.FONT_SIZE_TEXT);
        run.setItalic(italic);
    }

    //Metodo para adicionar/Formatar titulos pós textuais
    public void addPostTextualTitle(XWPFDocument doc, String text, String fontFamily) {
        XWPFParagraph paragraph = doc.createParagraph();

        paragraph.setStyle("Heading1");
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        breakLines(doc, 1);

        XWPFRun run = paragraph.createRun();
        run.setText(text.toUpperCase());
        run.setBold(true);
        run.setFontFamily(fontFamily);
        run.setFontSize(AbntConstants.FONT_SIZE_TEXT);
    }

    //Metodo para adicionar/formatar texto de referencia
    public void addReferenceText(XWPFDocument doc, String text, String fontFamily){
        if (text == null || text.isBlank()) return;

        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        paragraph.setSpacingBetween(AbntConstants.SIMPLE_LINE_SPACING);
        breakLines(doc, 1);

        paragraph.setIndentationFirstLine(AbntConstants.NO_INDENTATION_FIRST_LINE);
        paragraph.setIndentationLeft(AbntConstants.NO_INDENTATION);

        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setFontFamily(fontFamily);
        run.setFontSize(AbntConstants.FONT_SIZE_TITLE);
    }

    //Metodo para adicionar a tabela de errata
    public void addErrataTable(XWPFDocument doc, List<ErrataItem> items, String fontFamily){
        if (items == null || items.isEmpty()) return;

        XWPFTable table = doc.createTable();
        table.setWidth(AbntConstants.TABLE_WIDTH_FULL);

        XWPFTable.XWPFBorderType type = XWPFTable.XWPFBorderType.SINGLE;

        table.setTopBorder(type, AbntConstants.BORDER_SIZE, AbntConstants.BORDER_SPACE, AbntConstants.BORDER_COLOR);
        table.setBottomBorder(type, AbntConstants.BORDER_SIZE, AbntConstants.BORDER_SPACE, AbntConstants.BORDER_COLOR);
        table.setLeftBorder(type, AbntConstants.BORDER_SIZE, AbntConstants.BORDER_SPACE, AbntConstants.BORDER_COLOR);
        table.setRightBorder(type, AbntConstants.BORDER_SIZE, AbntConstants.BORDER_SPACE, AbntConstants.BORDER_COLOR);
        table.setInsideHBorder(type, AbntConstants.BORDER_SIZE, AbntConstants.BORDER_SPACE, AbntConstants.BORDER_COLOR);
        table.setInsideVBorder(type, AbntConstants.BORDER_SIZE, AbntConstants.BORDER_SPACE, AbntConstants.BORDER_COLOR);

        XWPFTableRow header = table.getRow(0);
        styleCell(header.getCell(0), "Folha", true, fontFamily, ParagraphAlignment.CENTER, AbntConstants.TABLE_CELL_WIDTH_SMALL);
        styleCell(header.addNewTableCell(), "Linha", true, fontFamily, ParagraphAlignment.CENTER, AbntConstants.TABLE_CELL_WIDTH_SMALL);
        styleCell(header.addNewTableCell(), "Onde se lê", true, fontFamily, ParagraphAlignment.CENTER, AbntConstants.TABLE_CELL_WIDTH_LARGE);
        styleCell(header.addNewTableCell(), "Leia-se", true, fontFamily, ParagraphAlignment.CENTER, AbntConstants.TABLE_CELL_WIDTH_LARGE);

        for (var item : items){
            XWPFTableRow row = table.createRow();
            styleCell(row.getCell(0), String.valueOf(item.getPage()), false, fontFamily, ParagraphAlignment.CENTER, AbntConstants.TABLE_CELL_WIDTH_SMALL);
            styleCell(row.getCell(1), String.valueOf(item.getLine()), false, fontFamily, ParagraphAlignment.CENTER, AbntConstants.TABLE_CELL_WIDTH_SMALL);
            styleCell(row.getCell(2), item.getTextFrom(), false, fontFamily, ParagraphAlignment.CENTER, AbntConstants.TABLE_CELL_WIDTH_LARGE);
            styleCell(row.getCell(3), item.getTextTo(), false, fontFamily, ParagraphAlignment.CENTER, AbntConstants.TABLE_CELL_WIDTH_LARGE);
        }

        breakLines(doc, 2);
    }

    //Metodo auxiliar do addErrataTable para formatar celulas
    private void styleCell(XWPFTableCell cell, String text, boolean bold, String fontFamily, ParagraphAlignment align, int width){
        if (cell.getParagraphs().size() > 0){
            for (int i = 0; i < cell.getParagraphs().size(); i++){
                cell.removeParagraph(0);
            }
        }

        if (width > 0){
            cell.setWidth(String.valueOf(width));
        }

        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        XWPFParagraph paragraph = cell.addParagraph();
        paragraph.setAlignment(align);
        paragraph.setSpacingAfter(AbntConstants.NO_SPACING);
        paragraph.setSpacingBefore(AbntConstants.NO_SPACING);

        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setBold(bold);
        run.setFontFamily(fontFamily);
        run.setFontSize(AbntConstants.FONT_SIZE_SMALL);
    }

    public void setupAbntPage(XWPFDocument doc) {
        // Garante que existe o corpo do documento
        if (doc.getDocument().getBody() == null) {
            doc.getDocument().addNewBody();
        }

        CTSectPr sectPr = doc.getDocument().getBody().isSetSectPr() ?
                doc.getDocument().getBody().getSectPr() :
                doc.getDocument().getBody().addNewSectPr();

        //Configurar Tamanho do Papel (A4)
        CTPageSz pageSize = sectPr.isSetPgSz() ? sectPr.getPgSz() : sectPr.addNewPgSz();
        pageSize.setW(BigInteger.valueOf(AbntConstants.PAGE_WIDTH));
        pageSize.setH(BigInteger.valueOf(AbntConstants.PAGE_HEIGHT));

        //Configurar Margens ABNT
        CTPageMar pageMar = sectPr.isSetPgMar() ? sectPr.getPgMar() : sectPr.addNewPgMar();
        pageMar.setTop(BigInteger.valueOf(AbntConstants.MARGIN_TOP));
        pageMar.setLeft(BigInteger.valueOf(AbntConstants.MARGIN_LEFT));
        pageMar.setBottom(BigInteger.valueOf(AbntConstants.MARGIN_BOTTOM));
        pageMar.setRight(BigInteger.valueOf(AbntConstants.MARGIN_RIGHT));
    }

}
