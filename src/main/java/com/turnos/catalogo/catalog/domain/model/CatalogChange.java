package com.turnos.catalogo.catalog.domain.model;

import java.util.List;

// Cambios de una versión del catálogo con el estado vigente de cada entidad afectada.
// Redis da solo los ids; el estado se lee de los hashes (contrato 14.5).
public record CatalogChange(
        long version,
        List<ProfessionalCategory> categories,
        List<Professional> professionals,
        List<WeeklySchedule> schedules) {
}
