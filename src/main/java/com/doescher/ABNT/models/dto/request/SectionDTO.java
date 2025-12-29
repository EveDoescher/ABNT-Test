package com.doescher.ABNT.models.dto.request;

import java.util.List;

public record SectionDTO(String title, String content, Integer sectionOrder, List<SectionDTO> subSections) {
}
