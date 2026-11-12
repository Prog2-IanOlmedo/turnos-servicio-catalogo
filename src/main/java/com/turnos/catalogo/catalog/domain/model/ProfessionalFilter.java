package com.turnos.catalogo.catalog.domain.model;

// Criterios opcionales de búsqueda de profesionales. Un criterio en null (o un nombre
// vacío) no filtra. Los deshabilitados nunca se muestran.
public record ProfessionalFilter(Long categoryId, String name) {

    public static ProfessionalFilter none() {
        return new ProfessionalFilter(null, null);
    }

    public boolean accepts(Professional professional) {
        return professional.isEnabled()
                && (categoryId == null || professional.belongsToCategory(categoryId))
                && (name == null || name.isBlank() || professional.nameContains(name));
    }
}
