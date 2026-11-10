package com.turnos.catalogo.catalog.infrastructure.messaging;

import com.turnos.catalogo.catalog.application.service.CatalogService;
import com.turnos.catalogo.catalog.domain.model.SyncResult;
import com.turnos.catalogo.catalog.infrastructure.messaging.dto.CatalogUpdatedEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

// Kafka es solo el aviso (sección 16). Un aviso repetido no cambia nada porque la
// puesta al día compara versiones. Si falla, el chequeo periódico lo vuelve a intentar,
// así que no relanzo la excepción para no trabar el topic con reintentos.
@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogUpdatedListener {

    private static final String CATALOG_UPDATED = "CatalogUpdated";

    private final CatalogService catalogService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${catedra.kafka.catalog-topic}")
    public void onMessage(String payload) {
        CatalogUpdatedEventDto event;
        try {
            event = objectMapper.readValue(payload, CatalogUpdatedEventDto.class);
        } catch (JacksonException e) {
            log.warn("Aviso de catálogo ilegible, se ignora: {}", e.getOriginalMessage());
            return;
        }
        if (!CATALOG_UPDATED.equals(event.eventType())) {
            log.warn("Evento de catálogo desconocido, se ignora: {}", event.eventType());
            return;
        }
        try {
            SyncResult result = catalogService.catchUpCatalog();
            log.info("CatalogUpdated {} (versión {}): {}", event.eventId(), event.newVersion(), result);
        } catch (RuntimeException e) {
            log.warn("No se pudo sincronizar tras CatalogUpdated {}: {}", event.eventId(), e.getMessage());
        }
    }
}
