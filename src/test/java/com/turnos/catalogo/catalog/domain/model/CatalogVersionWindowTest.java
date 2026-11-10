package com.turnos.catalogo.catalog.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogVersionWindowTest {

    private final CatalogVersionWindow ventana = new CatalogVersionWindow(148, 49);

    @Test
    void sinVersionLocalHayQueSincronizarYNoSePuedeSeguirPorIncrementos() {
        assertThat(ventana.isAheadOf(null)).isTrue();
        assertThat(ventana.canContinueFrom(null)).isFalse();
    }

    @Test
    void unaVersionLocalDentroDeLaVentanaSigueSiendoIncremental() {
        assertThat(ventana.isAheadOf(146L)).isTrue();
        assertThat(ventana.canContinueFrom(146L)).isTrue();
        assertThat(ventana.canContinueFrom(49L)).isTrue();
    }

    @Test
    void unaVersionLocalAnteriorALaVentanaNecesitaElSnapshot() {
        // Mismo caso que la sección 18.2 del contrato.
        assertThat(ventana.canContinueFrom(1L)).isFalse();
        assertThat(ventana.canContinueFrom(48L)).isFalse();
    }

    @Test
    void conLaVersionActualYaEstaAlDia() {
        assertThat(ventana.isAheadOf(148L)).isFalse();
    }
}
