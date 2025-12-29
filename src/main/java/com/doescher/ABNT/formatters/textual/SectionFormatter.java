package com.doescher.ABNT.formatters.textual;

import com.doescher.ABNT.models.entities.Document;
import com.doescher.ABNT.models.entities.Section;
import com.doescher.ABNT.helpers.WordHelper;
import com.doescher.ABNT.formatters.ComponentFormatter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@Order(10)
@RequiredArgsConstructor
public class SectionFormatter implements ComponentFormatter {

    private final WordHelper wordHelper;

    @Override
    public boolean shouldRender(Document data){
        return data.getSections() != null && !data.getSections().isEmpty();
    }

    @Override
    public void format(XWPFDocument doc, Document data){
        String font = data.getFontType().getFamilyName();

        List<Section> rootSections = data.getSections().stream()
                .filter(section -> section.getParent() == null)
                .sorted(Comparator.comparing(Section::getSectionOrder))
                .toList();

        for (Section section : rootSections){
            renderRecursive(doc, section, String.valueOf(section.getSectionOrder()), 1, font, wordHelper);
        }
    }

    private  void renderRecursive(XWPFDocument doc, Section section, String hierarchy, int level,String font, WordHelper wordHelper){
        if (level > 1){
            wordHelper.breakLines(doc, 1);
        }

        String titleText = hierarchy + " " + (level == 1 ? section.getTitle().toUpperCase() : section.getTitle());
        wordHelper.applyHeading(doc, level, titleText, font);

        wordHelper.breakLines(doc, 1);

        if (section.getContent() != null && !section.getContent().isBlank()){
            wordHelper.addBodyText(doc, section.getContent(), font);
        }

        if (section.getSubSections() != null){
            section.getSubSections().stream()
                    .sorted(Comparator.comparing(Section::getSectionOrder))
                    .forEach(child -> renderRecursive(doc, child, hierarchy + "." + child.getSectionOrder(), level + 1, font, wordHelper));
        }
    }
}
