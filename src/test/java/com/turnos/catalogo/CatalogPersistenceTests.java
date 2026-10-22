package com.turnos.catalogo;

import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.domain.model.ProfessionalCategory;
import com.turnos.catalogo.catalog.domain.model.WeeklySchedule;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalCategoryRepository;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalRepository;
import com.turnos.catalogo.catalog.domain.ports.out.WeeklyScheduleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CatalogPersistenceTests {

    private static final Instant FECHA = Instant.parse("2026-07-01T12:00:00Z");

    @Autowired
    private ProfessionalCategoryRepository categoryRepository;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private WeeklyScheduleRepository scheduleRepository;

    // Datos tomados del ejemplo de snapshot del contrato.
    @Test
    void guardaYRecuperaCategoriaProfesionalYAgenda() {
        categoryRepository.save(ProfessionalCategory.builder()
                .id(1L).name("Clinica medica").description("Atencion clinica general")
                .enabled(true).createdAt(FECHA).updatedAt(FECHA).build());

        professionalRepository.save(Professional.builder()
                .id(15L).categoryId(1L).firstName("Ana").lastName("Gomez")
                .enabled(true).createdAt(FECHA).updatedAt(FECHA).build());

        scheduleRepository.save(WeeklySchedule.builder()
                .id(21L).professionalId(15L).dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(9, 0)).endTime(LocalTime.of(12, 0))
                .slotDurationMinutes(30).enabled(true)
                .createdAt(FECHA).updatedAt(FECHA).build());

        ProfessionalCategory categoria = categoryRepository.findById(1L).orElseThrow();
        assertThat(categoria.getName()).isEqualTo("Clinica medica");
        assertThat(categoria.getCreatedAt()).isEqualTo(FECHA);

        Professional profesional = professionalRepository.findById(15L).orElseThrow();
        assertThat(profesional.getCategoryId()).isEqualTo(1L);
        assertThat(profesional.getLastName()).isEqualTo("Gomez");

        WeeklySchedule agenda = scheduleRepository.findById(21L).orElseThrow();
        assertThat(agenda.getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(agenda.getStartTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(agenda.getSlotDurationMinutes()).isEqualTo(30);
    }

    // Al sincronizar, el mismo id puede llegar de nuevo con cambios.
    @Test
    void guardarConUnIdExistenteActualizaElRegistro() {
        categoryRepository.save(ProfessionalCategory.builder()
                .id(2L).name("Pediatria").enabled(true)
                .createdAt(FECHA).updatedAt(FECHA).build());

        categoryRepository.save(ProfessionalCategory.builder()
                .id(2L).name("Pediatria").enabled(false)
                .createdAt(FECHA).updatedAt(FECHA).build());

        assertThat(categoryRepository.findById(2L).orElseThrow().isEnabled()).isFalse();
        assertThat(categoryRepository.findAll())
                .filteredOn(c -> c.getId().equals(2L))
                .hasSize(1);
    }

    @Test
    void buscaLasAgendasDeUnProfesional() {
        categoryRepository.save(ProfessionalCategory.builder()
                .id(3L).name("Kinesiologia").enabled(true)
                .createdAt(FECHA).updatedAt(FECHA).build());
        professionalRepository.save(Professional.builder()
                .id(30L).categoryId(3L).firstName("Luis").lastName("Perez")
                .enabled(true).createdAt(FECHA).updatedAt(FECHA).build());
        scheduleRepository.save(WeeklySchedule.builder()
                .id(31L).professionalId(30L).dayOfWeek(DayOfWeek.TUESDAY)
                .startTime(LocalTime.of(14, 0)).endTime(LocalTime.of(18, 0))
                .slotDurationMinutes(20).enabled(true)
                .createdAt(FECHA).updatedAt(FECHA).build());

        assertThat(scheduleRepository.findByProfessionalId(30L))
                .extracting(WeeklySchedule::getId)
                .containsExactly(31L);
    }
}
