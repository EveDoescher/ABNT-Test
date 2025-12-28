package com.doescher.ABNT.models.dto;

import java.util.List;

public record SectionDTO(String title, String content, Integer sectionOrder, List<SectionDTO> subSections) {
}
