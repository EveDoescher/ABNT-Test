package com.doescher.ABNT.mappers;

import com.doescher.ABNT.models.dto.DocumentDTO;
import com.doescher.ABNT.models.dto.ErrataItemDTO;
import com.doescher.ABNT.models.dto.SectionDTO;
import com.doescher.ABNT.models.entities.Document;
import com.doescher.ABNT.models.entities.ErrataItem;
import com.doescher.ABNT.models.entities.Section;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DocumentMapper {

    public Document toEntity(DocumentDTO dto){
        if (dto == null) return null;

        Document doc = new Document();

        // Configurações Gerais
        doc.setFontType(dto.fontType() != null ? dto.fontType() : "Arial");

        //Capa
        if (dto.cover() != null){
            doc.setInstitution(dto.cover().institution());
            doc.setCourse(dto.cover().course());
            doc.setTitle(dto.cover().title());
            doc.setSubtitle(dto.cover().subtitle());
            doc.setCity(dto.cover().city());
            doc.setAuthors(dto.cover().authors());
        }

        //Folha de Rosto
        if (dto.titlePage() != null){
            doc.setProjectNote(dto.titlePage().projectNote());
            doc.setAdvisor(dto.titlePage().advisor());
        }

        //Resumos
        //PTBR
        if (dto.nativeAbstract() != null){
            doc.setAbstractContent(dto.nativeAbstract().content());
            doc.setAbstractKeywords(dto.nativeAbstract().keywords());
        }

        //ENG
        if (dto.foreignAbstract() != null){
            doc.setForeignAbstractContent(dto.foreignAbstract().content());
            doc.setForeignAbstractKeywords(dto.foreignAbstract().keywords());
        }

        //Errata
        if (dto.errata() != null && !dto.errata().isEmpty()) {
            List<ErrataItem> errataList = dto.errata().stream()
                    .map(itemDto -> toErrataEntity(itemDto, doc))
                    .collect(Collectors.toList());
            doc.setErrata(errataList);
        }

        //Seções
        doc.setSections(new ArrayList<>());
        if (dto.sections() != null) {
            dto.sections().forEach(sectionDTO ->
                    mapSectionRecursive(sectionDTO, null, doc)
            );
        }

        //Referencias
        if (dto.references() != null) {
            doc.setReferences(dto.references().items());
        }

        return doc;
    }

    private ErrataItem toErrataEntity(ErrataItemDTO dto, Document doc){
        ErrataItem item = new ErrataItem();

        item.setPage(dto.page());
        item.setLine(dto.line());
        item.setTextFrom(dto.textFrom());
        item.setTextTo(dto.textTo());
        item.setDocument(doc);

        return item;
    }

    private void mapSectionRecursive(SectionDTO dto, Section parent, Document doc){
        Section section = new Section();

        section.setTitle(dto.title());
        section.setContent(dto.content());
        section.setSectionOrder(dto.sectionOrder());
        section.setParent(parent);

        doc.addSection(section);

        if (dto.subSections() != null && !dto.subSections().isEmpty()) {
            dto.subSections().forEach(subDto ->
                    mapSectionRecursive(subDto, section, doc)
            );
        }
    }
}
