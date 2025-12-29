package com.doescher.ABNT.models.entities;

import com.doescher.ABNT.models.enums.FontType;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private FontType fontType;

    //Capa
    private String institution;
    private String course;
    private String title;
    private String subtitle;
    private String city;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "document_authors", joinColumns = @JoinColumn(name = "document_id"))
    @Column(name = "author_name")
    private List<String> authors = new ArrayList<>();

    //Folha de rosto
    private String projectNote;
    private String advisor;

    //Errata (OPT)
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ErrataItem> errata = new ArrayList<>();

    //Dedicatoria (OPT)
    private String dedication;

    //Agradecimentos (OPT)
    private String acknowledgment;

    //Epigrafe (OPT)
    private String epigraph;

    //Resumo PTBR
    @Column(columnDefinition = "TEXT")
    private String abstractContent;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> abstractKeywords = new ArrayList<>();

    //Resumo ENG
    @Column(columnDefinition = "TEXT")
    private String foreignAbstractContent;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> foreignAbstractKeywords = new ArrayList<>();

    //Seções
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        sections.add(section);
        section.setDocument(this);
    }

    //Referencias
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tb_document_references", joinColumns = @JoinColumn(name = "document_id"))
    @Column(name = "reference_content", columnDefinition = "TEXT")
    private List<String> references = new ArrayList<>();
}