package com.turnos.catalogo;

import com.turnos.catalogo.catalog.domain.model.CatalogSnapshot;
import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.domain.model.ProfessionalCategory;
import com.turnos.catalogo.catalog.domain.model.WeeklySchedule;
import com.turnos.catalogo.catalog.domain.ports.in.ApplyCatalogSnapshotUseCase;
import com.turnos.catalogo.catalog.domain.ports.out.CatalogVersionRepository;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CatalogSnapshotIntegrationTests {

    private static final Instant FECHA = Instant.parse("2026-07-01T12:00:00Z");

    @Autowired
    private ApplyCatalogSnapshotUseCase applyCatalogSnapshotUseCase;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private CatalogVersionRepository versionRepository;

    // Ids altos para no pisar los datos de los otros tests de persistencia.
    @Test
    void aplicaUnSnapshotCompletoUnaSolaVez() {
        CatalogSnapshot snapshot = CatalogSnapshot.builder()
                .snapshotVersion(500).generatedAt(FECHA)
                .categories(List.of(ProfessionalCategory.builder().id(100L).name("Odontologia")
                        .enabled(true).createdAt(FECHA).updatedAt(FECHA).build()))
                .professionals(List.of(Professional.builder().id(150L).categoryId(100L)
                        .firstName("Laura").lastName("Diaz").enabled(true).createdAt(FECHA).updatedAt(FECHA).build()))
                .schedules(List.of(WeeklySchedule.builder().id(210L).professionalId(150L)
                        .dayOfWeek(DayOfWeek.FRIDAY).startTime(LocalTime.of(8, 0)).endTime(LocalTime.of(12, 0))
                        .slotDurationMinutes(30).enabled(true).createdAt(FECHA).updatedAt(FECHA).build()))
                .build();

        assertThat(applyCatalogSnapshotUseCase.applySnapshot(snapshot)).isTrue();
        assertThat(professionalRepository.findById(150L)).isPresent();
        assertThat(versionRepository.findCurrentVersion()).contains(500L);

        assertThat(applyCatalogSnapshotUseCase.applySnapshot(snapshot)).isFalse();
    }
}
