package com.turnos.catalogo.catalog.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.*;

// Copia completa del catálogo de la cátedra en una versión dada (contrato, sección 7).
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogSnapshot {
    private long snapshotVersion;
    private Instant generatedAt;
    @Builder.Default
    private List<ProfessionalCategory> categories = List.of();
    @Builder.Default
    private List<Professional> professionals = List.of();
    @Builder.Default
    private List<WeeklySchedule> schedules = List.of();

    // Sin versión local (null), cualquier snapshot es nuevo.
    public boolean isNewerThan(Long currentVersion) {
        return currentVersion == null || snapshotVersion > currentVersion;
    }

    // Cada profesional apunta a una categoría del snapshot y cada agenda
    // a un profesional del snapshot.
    public boolean hasConsistentReferences() {
        Set<Long> categoryIds = categories.stream()
                .map(ProfessionalCategory::getId)
                .collect(Collectors.toSet());
        Set<Long> professionalIds = professionals.stream()
                .map(Professional::getId)
                .collect(Collectors.toSet());
        return professionals.stream().allMatch(p -> categoryIds.contains(p.getCategoryId()))
                && schedules.stream().allMatch(s -> professionalIds.contains(s.getProfessionalId()));
    }
}
