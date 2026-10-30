package com.turnos.catalogo.catalog.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogSnapshotTest {

    private final ProfessionalCategory clinica = ProfessionalCategory.builder().id(1L).build();
    private final Professional ana = Professional.builder().id(15L).categoryId(1L).build();
    private final WeeklySchedule lunes = WeeklySchedule.builder().id(21L).professionalId(15L).build();

    @Test
    void elPrimerSnapshotSiempreEsNuevo() {
        assertThat(CatalogSnapshot.builder().snapshotVersion(1).build().isNewerThan(null)).isTrue();
    }

    @Test
    void soloEsNuevoSiSuVersionEsMayor() {
        CatalogSnapshot version7 = CatalogSnapshot.builder().snapshotVersion(7).build();

        assertThat(version7.isNewerThan(6L)).isTrue();
        assertThat(version7.isNewerThan(7L)).isFalse();
        assertThat(version7.isNewerThan(8L)).isFalse();
    }

    @Test
    void reconoceReferenciasCoherentes() {
        CatalogSnapshot snapshot = CatalogSnapshot.builder()
                .categories(List.of(clinica)).professionals(List.of(ana)).schedules(List.of(lunes)).build();

        assertThat(snapshot.hasConsistentReferences()).isTrue();
    }

    @Test
    void detectaUnProfesionalConCategoriaInexistente() {
        CatalogSnapshot snapshot = CatalogSnapshot.builder()
                .professionals(List.of(ana)).build();

        assertThat(snapshot.hasConsistentReferences()).isFalse();
    }

    @Test
    void detectaUnaAgendaConProfesionalInexistente() {
        CatalogSnapshot snapshot = CatalogSnapshot.builder()
                .categories(List.of(clinica)).schedules(List.of(lunes)).build();

        assertThat(snapshot.hasConsistentReferences()).isFalse();
    }
}
