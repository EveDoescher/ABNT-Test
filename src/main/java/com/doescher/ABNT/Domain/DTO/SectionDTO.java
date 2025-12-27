package com.doescher.ABNT.Domain.DTO;

import java.util.List;

public record SectionDTO(String title, String content, Integer sectionOrder, List<SectionDTO> subSections) {
}
