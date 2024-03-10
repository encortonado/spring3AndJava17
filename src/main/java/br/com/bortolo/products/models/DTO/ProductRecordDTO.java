package br.com.bortolo.products.models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRecordDTO(@NotBlank String name, @NotNull BigDecimal value) {
}
