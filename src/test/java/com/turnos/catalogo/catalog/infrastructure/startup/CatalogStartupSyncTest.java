package com.turnos.catalogo.catalog.infrastructure.startup;

import com.turnos.catalogo.catalog.application.exception.SnapshotUnavailableException;
import com.turnos.catalogo.catalog.application.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogStartupSyncTest {

    @Mock
    private CatalogService catalogService;

    @Test
    void sincronizaAlArrancarSiEstaHabilitado() {
        when(catalogService.synchronizeCatalog()).thenReturn(true);

        new CatalogStartupSync(catalogService, true).run(null);

        verify(catalogService).synchronizeCatalog();
    }

    @Test
    void noHaceNadaSiEstaDeshabilitado() {
        new CatalogStartupSync(catalogService, false).run(null);

        verify(catalogService, never()).synchronizeCatalog();
    }

    @Test
    void noFallaSiElCatalogoYaEstabaAlDia() {
        when(catalogService.synchronizeCatalog()).thenReturn(false);
        CatalogStartupSync sync = new CatalogStartupSync(catalogService, true);

        assertThatCode(() -> sync.run(null)).doesNotThrowAnyException();
        verify(catalogService).synchronizeCatalog();
    }

    @Test
    void siLaCatedraFallaLaAppArrancaIgual() {
        when(catalogService.synchronizeCatalog())
                .thenThrow(new SnapshotUnavailableException("sin respuesta"));
        CatalogStartupSync sync = new CatalogStartupSync(catalogService, true);

        assertThatCode(() -> sync.run(null)).doesNotThrowAnyException();
    }
}
