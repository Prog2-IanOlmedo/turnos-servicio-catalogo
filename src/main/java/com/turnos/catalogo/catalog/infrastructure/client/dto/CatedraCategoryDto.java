package com.turnos.catalogo.catalog.infrastructure.client.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CatedraCategoryDto(
        Long id,
        String name,
        String description,
        boolean enabled,
        Instant createdAt,
        Instant updatedAt) {
}
