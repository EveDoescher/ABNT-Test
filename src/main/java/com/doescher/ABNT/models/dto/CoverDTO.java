package com.doescher.ABNT.models.dto;

import java.util.List;

public record CoverDTO(String institution, String course, List<String> authors, String title, String subtitle, String city) {
}
