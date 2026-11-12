package com.turnos.catalogo.catalog.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfessionalFilterTest {

    private final Professional rivas = profesional(101L, "Martin", "Rivas", true);

    private Professional profesional(Long categoria, String nombre, String apellido, boolean habilitado) {
        return Professional.builder().id(1L).categoryId(categoria).firstName(nombre).lastName(apellido)
                .enabled(habilitado).build();
    }

    @Test
    void sinCriteriosAceptaLosHabilitados() {
        assertThat(ProfessionalFilter.none().accepts(rivas)).isTrue();
    }

    @Test
    void nuncaAceptaUnDeshabilitado() {
        assertThat(ProfessionalFilter.none().accepts(profesional(101L, "Ana", "Paz", false))).isFalse();
    }

    @Test
    void filtraPorCategoria() {
        assertThat(new ProfessionalFilter(101L, null).accepts(rivas)).isTrue();
        assertThat(new ProfessionalFilter(102L, null).accepts(rivas)).isFalse();
    }

    @Test
    void filtraPorNombreOApellidoSinImportarMayusculas() {
        assertThat(new ProfessionalFilter(null, "RIV").accepts(rivas)).isTrue();
        assertThat(new ProfessionalFilter(null, " martin ").accepts(rivas)).isTrue();
        assertThat(new ProfessionalFilter(null, "martin rivas").accepts(rivas)).isTrue();
        assertThat(new ProfessionalFilter(null, "gomez").accepts(rivas)).isFalse();
    }

    @Test
    void unNombreVacioNoFiltra() {
        assertThat(new ProfessionalFilter(null, "  ").accepts(rivas)).isTrue();
    }

    @Test
    void combinaLosCriterios() {
        assertThat(new ProfessionalFilter(101L, "rivas").accepts(rivas)).isTrue();
        assertThat(new ProfessionalFilter(102L, "rivas").accepts(rivas)).isFalse();
    }

    @Test
    void unProfesionalSinCategoriaNoPerteneceANinguna() {
        assertThat(profesional(null, "Ana", null, true).belongsToCategory(101L)).isFalse();
        assertThat(profesional(null, "Ana", null, true).nameContains("ana")).isTrue();
    }
}
