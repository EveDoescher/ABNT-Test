package com.doescher.ABNT.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ErrataItemDTO(
        @NotNull(message = "O número da folha é obrigatório")
        Integer page,
        @NotNull(message = "O número da linha é obrigatório")
        Integer line,
        @NotBlank(message = "O texto original ('Onde se lê') é obrigatório")
        String textFrom,
        @NotBlank(message = "O texto corrigido ('Leia-se') é obrigatório")
        String textTo
) {
}
