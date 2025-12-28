package com.doescher.ABNT.Formatters.Textual;

import com.doescher.ABNT.Domain.Models.Document;
import com.doescher.ABNT.Domain.Models.Section;
import com.doescher.ABNT.Engine.WordEngine;
import com.doescher.ABNT.Formatters.ComponentFormatter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@Order(7)
public class SectionFormatter implements ComponentFormatter {

    @Override
    public boolean shouldRender(Document data){
        return data.getSections() != null && !data.getSections().isEmpty();
    }

    @Override
    public void format(XWPFDocument doc, Document data, WordEngine engine){
        String font = data.getFontType();

        List<Section> rootSections = data.getSections().stream()
                .filter(section -> section.getParent() == null)
                .sorted(Comparator.comparing(Section::getSectionOrder))
                .toList();

        for (Section section : rootSections){
            renderRecursive(doc, section, String.valueOf(section.getSectionOrder()), 1, font, engine);
        }
    }

    private  void renderRecursive(XWPFDocument doc, Section section, String hierarchy, int level,String font, WordEngine engine){
        if (level > 1){
            engine.breakLines(doc, 1);
        }

        String titleText = hierarchy + " " + (level == 1 ? section.getTitle().toUpperCase() : section.getTitle());
        engine.applyHeading(doc, level, titleText, font);

        engine.breakLines(doc, 1);

        if (section.getContent() != null && !section.getContent().isBlank()){
            engine.addBodyText(doc, section.getContent(), font);
        }

        if (section.getSubSections() != null){
            section.getSubSections().stream()
                    .sorted(Comparator.comparing(Section::getSectionOrder))
                    .forEach(child -> renderRecursive(doc, child, hierarchy + "." + child.getSectionOrder(), level + 1, font, engine));
        }
    }
}
