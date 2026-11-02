package com.turnos.catalogo.catalog.infrastructure.client.dto;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Respuesta de GET /api/synchronization/snapshot tal como la manda la cátedra (sección 7).
@JsonIgnoreProperties(ignoreUnknown = true)
public record CatedraSnapshotDto(
        long snapshotVersion,
        Instant generatedAt,
        List<CatedraCategoryDto> professionalCategories,
        List<CatedraProfessionalDto> professionals,
        List<CatedraScheduleDto> weeklySchedules) {
}
