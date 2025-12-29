package com.doescher.ABNT.models.dto.request;

import com.doescher.ABNT.models.enums.FontType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DocumentRequest(
        FontType fontType,

        @NotNull(message = "Dados da capa são obrigatórios")
        @Valid
        CoverDTO cover,

        @NotNull(message = "Dados da folha de rosto são obrigatórios")
        @Valid
        TitlePageDTO titlePage,

        @NotNull(message = "Resumo em português é obrigatório")
        @Valid
        AbstractDTO nativeAbstract,

        @NotNull(message = "Abstract em inglês é obrigatório")
        @Valid
        AbstractDTO foreignAbstract,

        @Valid
        List<ErrataItemDTO> errata,

        String dedication,

        String acknowledgment,

        String epigraph,

        @NotEmpty(message = "O documento deve ter pelo menos uma seção")
        @Valid
        List<SectionDTO> sections,

        @NotNull(message = "Referências são obrigatórias")
        @Valid
        ReferenceDTO references
) {
}
