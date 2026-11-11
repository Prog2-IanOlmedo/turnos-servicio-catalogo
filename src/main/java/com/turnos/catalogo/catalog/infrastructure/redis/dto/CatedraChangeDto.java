package com.turnos.catalogo.catalog.infrastructure.redis.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Contenido de catedra:sync:changes:{version}: solo los ids afectados (contrato 14.5).
@JsonIgnoreProperties(ignoreUnknown = true)
public record CatedraChangeDto(long version, Changes changes) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Changes(List<Long> professionalCategories, List<Long> professionals, List<Long> weeklySchedules) {
    }
}
