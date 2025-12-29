package com.doescher.ABNT.mappers;

import com.doescher.ABNT.models.dto.request.*;
import com.doescher.ABNT.models.dto.response.DocumentDetailResponse;
import com.doescher.ABNT.models.dto.response.DocumentResponse;
import com.doescher.ABNT.models.entities.Document;
import com.doescher.ABNT.models.entities.ErrataItem;
import com.doescher.ABNT.models.entities.Section;
import com.doescher.ABNT.models.enums.FontType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DocumentMapper {

    //METODOS Request -> Entity
    public Document toEntity(DocumentRequest request){
        if (request == null) return null;

        Document doc = new Document();

        // Configurações Gerais
        doc.setFontType(request.fontType() != null ? request.fontType() : FontType.ARIAL);

        //Capa
        if (request.cover() != null){
            doc.setInstitution(request.cover().institution());
            doc.setCourse(request.cover().course());
            doc.setTitle(request.cover().title());
            doc.setSubtitle(request.cover().subtitle());
            doc.setCity(request.cover().city());
            doc.setAuthors(request.cover().authors());
        }

        //Folha de Rosto
        if (request.titlePage() != null){
            doc.setProjectNote(request.titlePage().projectNote());
            doc.setAdvisor(request.titlePage().advisor());
        }

        //Resumos
        //PTBR
        if (request.nativeAbstract() != null){
            doc.setAbstractContent(request.nativeAbstract().content());
            doc.setAbstractKeywords(request.nativeAbstract().keywords());
        }

        //ENG
        if (request.foreignAbstract() != null){
            doc.setForeignAbstractContent(request.foreignAbstract().content());
            doc.setForeignAbstractKeywords(request.foreignAbstract().keywords());
        }

        //Errata
        if (request.errata() != null && !request.errata().isEmpty()) {
            List<ErrataItem> errataList = request.errata().stream()
                    .map(itemDto -> toErrataEntity(itemDto, doc))
                    .collect(Collectors.toList());
            doc.setErrata(errataList);
        }

        //Dedicatoria
        if (request.dedication() != null){
            doc.setDedication(request.dedication());
        }

        //Agradecimentos
        if (request.acknowledgment() != null){
            doc.setAcknowledgment(request.acknowledgment());
        }

        //Epigrafe
        if (request.epigraph() != null){
            doc.setEpigraph(request.epigraph());
        }

        //Seções
        doc.setSections(new ArrayList<>());
        if (request.sections() != null) {
            request.sections().forEach(sectionDTO ->
                    mapSectionRecursive(sectionDTO, null, doc)
            );
        }

        //Referencias
        if (request.references() != null) {
            doc.setReferences(request.references().items());
        }

        return doc;
    }


    //--------------------------------------------------------
    //Metodos Entity -> Response
    public DocumentResponse toResponse(Document doc){
        if (doc == null) return null;
        return new DocumentResponse(
                doc.getId(),
                doc.getTitle(),
                doc.getInstitution()
        );
    }

    //Detail Response
    public DocumentDetailResponse toDetailResponse(Document doc) {
        if (doc == null) return null;

        //Capa
        CoverDTO coverDTO = new CoverDTO(
                doc.getInstitution(),
                doc.getCourse(),
                doc.getAuthors(),
                doc.getTitle(),
                doc.getSubtitle(),
                doc.getCity()
        );

        //Folha de Rosto
        TitlePageDTO titlePageDTO = new TitlePageDTO(
                doc.getAdvisor(),
                doc.getProjectNote()
        );

        //Resumos
        //PTBR
        AbstractDTO nativeAbstractDTO = new AbstractDTO(
                doc.getAbstractContent(),
                doc.getAbstractKeywords()
        );

        //ENG
        AbstractDTO foreignAbstractDTO = new AbstractDTO(
                doc.getForeignAbstractContent(),
                doc.getForeignAbstractKeywords()
        );

        //Errata
        List<ErrataItemDTO> errataDTOs = null;
        if (doc.getErrata() != null) {
            errataDTOs = doc.getErrata().stream()
                    .map(this::toErrataDTO)
                    .collect(Collectors.toList());
        }

        //Dedicatoria
        String dedication = new String(
                doc.getDedication()
        );

        //Agradecimentos
        String acknowledgment = new String(
                doc.getAcknowledgment()
        );

        //Epigrafe
        String epigraph = new String(
                doc.getEpigraph()
        );

        //Seções
        List<SectionDTO> sectionDTOs = new ArrayList<>();
        if (doc.getSections() != null) {
            sectionDTOs = doc.getSections().stream()
                    .filter(s -> s.getParent() == null)
                    .sorted(Comparator.comparing(Section::getId))
                    .map(this::toSectionDTO)
                    .collect(Collectors.toList());
        }

        //Referências
        ReferenceDTO referenceDTO = new ReferenceDTO(doc.getReferences());

        return new DocumentDetailResponse(
                doc.getId(),
                doc.getFontType(),
                coverDTO,
                titlePageDTO,
                nativeAbstractDTO,
                foreignAbstractDTO,
                errataDTOs,
                dedication,
                acknowledgment,
                epigraph,
                sectionDTOs,
                referenceDTO
        );
    }


    //--------------------------------------------------------
    //Helpers privados
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

    private ErrataItemDTO toErrataDTO(ErrataItem item) {
        return new ErrataItemDTO(
                item.getPage(),
                item.getLine(),
                item.getTextFrom(),
                item.getTextTo()
        );
    }

    private SectionDTO toSectionDTO(Section section) {

        List<SectionDTO> subSectionsDTO = new ArrayList<>();

        if (section.getSubSections() != null) {
            subSectionsDTO = section.getSubSections().stream()
                    .map(this::toSectionDTO)
                    .collect(Collectors.toList());
        }

        return new SectionDTO(
                section.getTitle(),
                section.getContent(),
                section.getSectionOrder(),
                subSectionsDTO
        );
    }
}
