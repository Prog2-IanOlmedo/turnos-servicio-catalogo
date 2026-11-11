package com.turnos.catalogo.catalog.infrastructure.messaging;

import com.turnos.catalogo.catalog.application.service.CatalogService;
import com.turnos.catalogo.catalog.domain.model.SyncResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogUpdatedListenerTest {

    private static final String AVISO = """
            {"eventId": "catalog-event-148-4f3b", "eventType": "CatalogUpdated", "newVersion": 148,
             "occurredAt": "2026-11-10T12:15:51Z", "source": "SCHEDULED", "schemaVersion": 1}
            """;

    @Mock
    private CatalogService catalogService;

    private CatalogUpdatedListener listener;

    @BeforeEach
    void setUp() {
        listener = new CatalogUpdatedListener(catalogService, JsonMapper.builder().build());
    }

    @Test
    void conCadaAvisoSePoneAlDia() {
        when(catalogService.catchUpCatalog()).thenReturn(SyncResult.INCREMENTAL);

        listener.onMessage(AVISO);

        verify(catalogService).catchUpCatalog();
    }

    @Test
    void ignoraUnEventoDeOtroTipo() {
        listener.onMessage("{\"eventId\": \"x\", \"eventType\": \"OtroEvento\"}");

        verify(catalogService, never()).catchUpCatalog();
    }

    @Test
    void ignoraUnMensajeIlegible() {
        listener.onMessage("esto no es json");

        verify(catalogService, never()).catchUpCatalog();
    }

    @Test
    void siLaSincronizacionFallaNoTrabaElTopic() {
        when(catalogService.catchUpCatalog()).thenThrow(new IllegalStateException("redis caído"));

        assertThatCode(() -> listener.onMessage(AVISO)).doesNotThrowAnyException();
    }
}
