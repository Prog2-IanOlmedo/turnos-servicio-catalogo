package com.turnos.catalogo.catalog.domain.model;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WeeklyScheduleTest {

    private WeeklySchedule agenda(String inicio, String fin, int duracion, boolean habilitada) {
        return WeeklySchedule.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.parse(inicio))
                .endTime(LocalTime.parse(fin))
                .slotDurationMinutes(duracion)
                .enabled(habilitada)
                .build();
    }

    @Test
    void divideElBloqueEnTurnosCompletos() {
        assertThat(agenda("09:00", "12:00", 30, true).slotStartTimes())
                .containsExactly(LocalTime.of(9, 0), LocalTime.of(9, 30), LocalTime.of(10, 0),
                        LocalTime.of(10, 30), LocalTime.of(11, 0), LocalTime.of(11, 30));
    }

    @Test
    void noOfreceUnTurnoQueNoEntraCompleto() {
        assertThat(agenda("09:00", "10:10", 30, true).slotStartTimes())
                .containsExactly(LocalTime.of(9, 0), LocalTime.of(9, 30));
    }

    @Test
    void rechazaUnaDuracionInvalida() {
        assertThatThrownBy(() -> agenda("09:00", "10:00", 0, true).slotStartTimes())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void ofreceElTurnoSoloSiCoincidenDiaHoraYEstaHabilitada() {
        WeeklySchedule agenda = agenda("09:00", "12:00", 30, true);
        assertThat(agenda.offersSlotAt(DayOfWeek.MONDAY, LocalTime.of(9, 30))).isTrue();
        assertThat(agenda.offersSlotAt(DayOfWeek.MONDAY, LocalTime.of(9, 15))).isFalse();
        assertThat(agenda.offersSlotAt(DayOfWeek.TUESDAY, LocalTime.of(9, 30))).isFalse();
        assertThat(agenda("09:00", "12:00", 30, false)
                .offersSlotAt(DayOfWeek.MONDAY, LocalTime.of(9, 30))).isFalse();
    }
}
