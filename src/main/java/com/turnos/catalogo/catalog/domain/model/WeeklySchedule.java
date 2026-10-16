package com.turnos.catalogo.catalog.domain.model;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklySchedule {
    private Long id;
    private Long professionalId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private int slotDurationMinutes;
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;
}
