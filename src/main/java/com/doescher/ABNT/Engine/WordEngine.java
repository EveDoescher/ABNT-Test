package com.doescher.ABNT.Engine;

import com.doescher.ABNT.Domain.Models.ErrataItem;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class WordEngine {

    //Metodo para adicionar paragrafo formatado
    public void addParagraph(XWPFDocument doc, String text, boolean bold, ParagraphAlignment align, int spacingAfter, String fontFamily){
        if (text == null) return;

        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.setAlignment(align);
        paragraph.setSpacingAfter(spacingAfter);
        paragraph.setSpacingBefore(0);
        paragraph.setSpacingBetween(1.5, LineSpacingRule.AUTO);

        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setBold(bold);
        run.setFontFamily(fontFamily);
        run.setFontSize(12);
    }

    //Metodo para pular linhas
    public void breakLines(XWPFDocument doc, int qnt){
        for (int i = 0; i < qnt; i++){
            XWPFParagraph paragraph = doc.createParagraph();
            paragraph.setSpacingAfter(0);
            paragraph.setSpacingBefore(0);
            paragraph.setSpacingBetween(1.5, LineSpacingRule.AUTO);
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
            paragraph.setSpacingAfter(0);
            paragraph.setSpacingBetween(1.5, LineSpacingRule.AUTO);
            paragraph.setIndentationFirstLine(709); //1.25cm

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
        paragraph.setSpacingBetween(1.5, LineSpacingRule.AUTO);
        paragraph.setIndentationFirstLine(0);

        XWPFRun run = paragraph.createRun();
        run.setText(content);
        run.setFontFamily(fontFamily);
        run.setFontSize(12);
    }

    //Metodo para configurar a criação do sumario
    public void addTOC(XWPFDocument doc, String fontFamily){
        addParagraph(doc, "SUMÁRIO", true, ParagraphAlignment.CENTER, 0, fontFamily);
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

    //Metodo para adicionar/Formatar titulos pós textuais
    public void addPostTextualTitle(XWPFDocument doc, String text, String fontFamily) {
        XWPFParagraph paragraph = doc.createParagraph();

        paragraph.setStyle("Heading1");
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        paragraph.setSpacingAfter(240);

        XWPFRun run = paragraph.createRun();
        run.setText(text.toUpperCase());
        run.setBold(true);
        run.setFontFamily(fontFamily);
        run.setFontSize(12);
    }

    //Metodo para adicionar/formatar texto de referencia
    public void addReferenceText(XWPFDocument doc, String text, String fontFamily){
        if (text == null || text.isBlank()) return;

        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        paragraph.setSpacingBetween(1.0);
        paragraph.setSpacingAfter(240);

        paragraph.setIndentationFirstLine(0);
        paragraph.setIndentationLeft(0);

        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setFontFamily(fontFamily);
        run.setFontSize(12);
    }

    //Metodo para adicionar a tabela de errata
    public void addErrataTable(XWPFDocument doc, List<ErrataItem> items, String fontFamily){
        if (items == null || items.isEmpty()) return;

        XWPFTable table = doc.createTable();
        table.setWidth("100%");

        XWPFTableRow header = table.getRow(0);
        header.getCell(0).setText("Folha");
        header.addNewTableCell().setText("Linha");
        header.addNewTableCell().setText("Onde se lê");
        header.addNewTableCell().setText("Leia-se");

        for (var item : items){
            XWPFTableRow row = table.createRow();
            row.getCell(0).setText(String.valueOf(item.getPage()));
            row.getCell(1).setText(String.valueOf(item.getLine()));
            row.getCell(2).setText(item.getTextFrom());
            row.getCell(3).setText(item.getTextTo());
        }

        breakLines(doc, 2);
    }

    public void setupAbntPage(XWPFDocument doc) {
        // Garante que existe o corpo do documento
        if (doc.getDocument().getBody() == null) {
            doc.getDocument().addNewBody();
        }

        CTSectPr sectPr = doc.getDocument().getBody().isSetSectPr() ?
                doc.getDocument().getBody().getSectPr() :
                doc.getDocument().getBody().addNewSectPr();

        // 1. Configurar Tamanho do Papel (A4)
        // Largura: 11906 twips (~21cm), Altura: 16838 twips (~29.7cm)
        CTPageSz pageSize = sectPr.isSetPgSz() ? sectPr.getPgSz() : sectPr.addNewPgSz();
        pageSize.setW(BigInteger.valueOf(11906));
        pageSize.setH(BigInteger.valueOf(16838));

        // 2. Configurar Margens ABNT (Sup/Esq: 3cm, Inf/Dir: 2cm)
        // 1cm = 567 twips
        // 3cm = 1701 twips
        // 2cm = 1134 twips
        CTPageMar pageMar = sectPr.isSetPgMar() ? sectPr.getPgMar() : sectPr.addNewPgMar();
        pageMar.setTop(BigInteger.valueOf(1701));    // 3cm
        pageMar.setLeft(BigInteger.valueOf(1701));   // 3cm
        pageMar.setBottom(BigInteger.valueOf(1134)); // 2cm
        pageMar.setRight(BigInteger.valueOf(1134));  // 2cm
    }

}
