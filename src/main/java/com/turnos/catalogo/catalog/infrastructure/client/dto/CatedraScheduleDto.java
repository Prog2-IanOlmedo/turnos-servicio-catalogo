package com.turnos.catalogo.catalog.infrastructure.client.dto;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Las horas llegan como HH:mm o HH:mm:ss; LocalTime acepta los dos formatos.
@JsonIgnoreProperties(ignoreUnknown = true)
public record CatedraScheduleDto(
        Long id,
        Long professionalId,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        int slotDurationMinutes,
        boolean enabled,
        Instant createdAt,
        Instant updatedAt) {
}
