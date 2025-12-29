package com.doescher.ABNT.models.dto.response;

import com.doescher.ABNT.models.dto.request.*;
import com.doescher.ABNT.models.enums.FontType;
import java.util.List;

public record DocumentDetailResponse(
        Long id,
        FontType fontType,
        CoverDTO cover,
        TitlePageDTO titlePage,
        AbstractDTO nativeAbstract,
        AbstractDTO foreignAbstract,
        List<ErrataItemDTO> errata,
        String dedication,
        String acknowledgment,
        String epigraph,
        List<SectionDTO> sections,
        ReferenceDTO references
) {
}
