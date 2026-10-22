package com.turnos.catalogo.catalog.domain.model;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    // Inicio de cada turno dentro del bloque. Un turno que no entra
    // completo antes de endTime no se ofrece.
    public List<LocalTime> slotStartTimes() {
        if (slotDurationMinutes <= 0) {
            throw new IllegalStateException("La duración del turno debe ser mayor a cero");
        }
        long minutosDisponibles = Duration.between(startTime, endTime).toMinutes();
        long cantidad = minutosDisponibles / slotDurationMinutes;
        List<LocalTime> slots = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            slots.add(startTime.plusMinutes((long) i * slotDurationMinutes));
        }
        return slots;
    }

    public boolean offersSlotAt(DayOfWeek day, LocalTime time) {
        return enabled && day.equals(dayOfWeek) && slotStartTimes().contains(time);
    }
}
