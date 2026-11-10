package com.turnos.catalogo.catalog.infrastructure.startup;

import com.turnos.catalogo.catalog.application.exception.SnapshotUnavailableException;
import com.turnos.catalogo.catalog.application.service.CatalogService;
import com.turnos.catalogo.catalog.domain.model.SyncResult;
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
    void alArrancarSePoneAlDia() {
        when(catalogService.catchUpCatalog()).thenReturn(SyncResult.SNAPSHOT);

        new CatalogStartupSync(catalogService, true).run(null);

        verify(catalogService).catchUpCatalog();
    }

    @Test
    void noHaceNadaSiEstaDeshabilitado() {
        new CatalogStartupSync(catalogService, false).run(null);

        verify(catalogService, never()).catchUpCatalog();
    }

    @Test
    void siLaCatedraFallaLaAppArrancaIgual() {
        when(catalogService.catchUpCatalog()).thenThrow(new SnapshotUnavailableException("sin respuesta"));
        CatalogStartupSync sync = new CatalogStartupSync(catalogService, true);

        assertThatCode(() -> sync.run(null)).doesNotThrowAnyException();
    }
}
