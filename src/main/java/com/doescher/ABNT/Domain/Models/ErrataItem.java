package com.doescher.ABNT.Domain.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ErrataItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer page;
    private Integer line;

    @Column(columnDefinition = "TEXT")
    private String textFrom;

    @Column(columnDefinition = "TEXT")
    private String textTo;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;
}
