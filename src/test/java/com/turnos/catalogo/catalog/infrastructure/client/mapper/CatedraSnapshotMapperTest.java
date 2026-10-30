package com.turnos.catalogo.catalog.infrastructure.client.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

import com.turnos.catalogo.catalog.domain.model.CatalogSnapshot;
import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.domain.model.ProfessionalCategory;
import com.turnos.catalogo.catalog.domain.model.WeeklySchedule;
import com.turnos.catalogo.catalog.infrastructure.client.dto.CatedraCategoryDto;
import com.turnos.catalogo.catalog.infrastructure.client.dto.CatedraProfessionalDto;
import com.turnos.catalogo.catalog.infrastructure.client.dto.CatedraScheduleDto;
import com.turnos.catalogo.catalog.infrastructure.client.dto.CatedraSnapshotDto;
import org.junit.jupiter.api.Test;

class CatedraSnapshotMapperTest {

    private static final Instant CREADO = Instant.parse("2026-07-01T12:00:00Z");
    private static final Instant ACTUALIZADO = Instant.parse("2026-09-10T13:40:00Z");

    private final CatedraSnapshotMapper mapper = new CatedraSnapshotMapper();

    @Test
    void pasaElSnapshotCompletoAlDominio() {
        CatedraSnapshotDto dto = new CatedraSnapshotDto(
                7L,
                Instant.parse("2026-09-10T13:44:59Z"),
                List.of(new CatedraCategoryDto(1L, "Clinica medica", "Atencion clinica general",
                        true, CREADO, ACTUALIZADO)),
                List.of(new CatedraProfessionalDto(15L, 1L, "Ana", "Gomez", true, CREADO, ACTUALIZADO)),
                List.of(new CatedraScheduleDto(21L, 15L, DayOfWeek.MONDAY, LocalTime.of(9, 0),
                        LocalTime.of(12, 0), 30, false, CREADO, ACTUALIZADO)));

        CatalogSnapshot snapshot = mapper.toDomainModel(dto);

        assertThat(snapshot.getSnapshotVersion()).isEqualTo(7L);
        assertThat(snapshot.getGeneratedAt()).isEqualTo(Instant.parse("2026-09-10T13:44:59Z"));

        assertThat(snapshot.getCategories()).hasSize(1);
        ProfessionalCategory categoria = snapshot.getCategories().get(0);
        assertThat(categoria.getId()).isEqualTo(1L);
        assertThat(categoria.getName()).isEqualTo("Clinica medica");
        assertThat(categoria.getDescription()).isEqualTo("Atencion clinica general");
        assertThat(categoria.isEnabled()).isTrue();
        assertThat(categoria.getCreatedAt()).isEqualTo(CREADO);
        assertThat(categoria.getUpdatedAt()).isEqualTo(ACTUALIZADO);

        assertThat(snapshot.getProfessionals()).hasSize(1);
        Professional profesional = snapshot.getProfessionals().get(0);
        assertThat(profesional.getId()).isEqualTo(15L);
        assertThat(profesional.getCategoryId()).isEqualTo(1L);
        assertThat(profesional.getFirstName()).isEqualTo("Ana");
        assertThat(profesional.getLastName()).isEqualTo("Gomez");
        assertThat(profesional.isEnabled()).isTrue();

        // La agenda viene deshabilitada a propósito: el mapper no filtra, eso lo decide el caso de uso.
        assertThat(snapshot.getSchedules()).hasSize(1);
        WeeklySchedule agenda = snapshot.getSchedules().get(0);
        assertThat(agenda.getId()).isEqualTo(21L);
        assertThat(agenda.getProfessionalId()).isEqualTo(15L);
        assertThat(agenda.getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(agenda.getStartTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(agenda.getEndTime()).isEqualTo(LocalTime.of(12, 0));
        assertThat(agenda.getSlotDurationMinutes()).isEqualTo(30);
        assertThat(agenda.isEnabled()).isFalse();

        assertThat(snapshot.hasConsistentReferences()).isTrue();
    }

    @Test
    void tomaLasListasQueFaltanComoVacias() {
        CatedraSnapshotDto dto = new CatedraSnapshotDto(3L, CREADO, null, null, null);

        CatalogSnapshot snapshot = mapper.toDomainModel(dto);

        assertThat(snapshot.getCategories()).isEmpty();
        assertThat(snapshot.getProfessionals()).isEmpty();
        assertThat(snapshot.getSchedules()).isEmpty();
        assertThat(snapshot.hasConsistentReferences()).isTrue();
    }

    @Test
    void devuelveNullSiNoHayRespuesta() {
        assertThat(mapper.toDomainModel(null)).isNull();
    }
}
