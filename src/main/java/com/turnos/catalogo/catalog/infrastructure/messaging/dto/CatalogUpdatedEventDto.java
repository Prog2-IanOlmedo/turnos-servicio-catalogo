package com.turnos.catalogo.catalog.infrastructure.messaging.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// CatalogUpdated (contrato 15.3). Solo avisa: los datos salen de Redis o del snapshot.
@JsonIgnoreProperties(ignoreUnknown = true)
public record CatalogUpdatedEventDto(String eventId, String eventType, Long newVersion, String source) {
}
