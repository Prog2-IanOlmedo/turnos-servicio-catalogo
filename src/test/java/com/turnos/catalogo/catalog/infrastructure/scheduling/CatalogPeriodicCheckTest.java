package com.turnos.catalogo.catalog.infrastructure.scheduling;

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
class CatalogPeriodicCheckTest {

    @Mock
    private CatalogService catalogService;

    @Test
    void chequeaLaVersionSiEstaHabilitado() {
        when(catalogService.catchUpCatalog()).thenReturn(SyncResult.INCREMENTAL);

        new CatalogPeriodicCheck(catalogService, true).check();

        verify(catalogService).catchUpCatalog();
    }

    @Test
    void siYaEstabaAlDiaNoPasaNada() {
        when(catalogService.catchUpCatalog()).thenReturn(SyncResult.UP_TO_DATE);

        assertThatCode(() -> new CatalogPeriodicCheck(catalogService, true).check()).doesNotThrowAnyException();
    }

    @Test
    void noHaceNadaSiEstaDeshabilitado() {
        new CatalogPeriodicCheck(catalogService, false).check();

        verify(catalogService, never()).catchUpCatalog();
    }

    @Test
    void unFalloNoCortaElChequeo() {
        when(catalogService.catchUpCatalog()).thenThrow(new IllegalStateException("redis caído"));

        assertThatCode(() -> new CatalogPeriodicCheck(catalogService, true).check()).doesNotThrowAnyException();
    }
}
