package com.turnos.catalogo.catalog.infrastructure.web.controller;

import com.turnos.catalogo.catalog.application.exception.ProfessionalNotFoundException;
import com.turnos.catalogo.catalog.application.service.CatalogService;
import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.domain.model.ProfessionalCategory;
import com.turnos.catalogo.catalog.domain.model.WeeklySchedule;
import com.turnos.catalogo.catalog.infrastructure.web.mapper.CatalogDtoMapper;
import com.turnos.catalogo.shared.web.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CatalogControllerTest {

    @Mock
    private CatalogService catalogService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        CatalogController controller = new CatalogController(catalogService, new CatalogDtoMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new CatalogExceptionHandler(), new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listaLasCategorias() throws Exception {
        when(catalogService.getAllCategories()).thenReturn(List.of(
                ProfessionalCategory.builder().id(1L).name("Clinica medica").enabled(true).build()));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Clinica medica"));
    }

    @Test
    void devuelveLasAgendasConSusTurnos() throws Exception {
        when(catalogService.getSchedulesByProfessional(15L)).thenReturn(List.of(
                WeeklySchedule.builder().id(21L).professionalId(15L).dayOfWeek(DayOfWeek.MONDAY)
                        .startTime(LocalTime.of(9, 0)).endTime(LocalTime.of(12, 0))
                        .slotDurationMinutes(30).enabled(true).build()));

        mockMvc.perform(get("/api/professionals/15/schedules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dayOfWeek").value("MONDAY"))
                .andExpect(jsonPath("$[0].slots.length()").value(6));
    }

    @Test
    void respondeProblemJsonSiElProfesionalNoExiste() throws Exception {
        when(catalogService.getProfessionalById(99L)).thenThrow(new ProfessionalNotFoundException(99L));

        mockMvc.perform(get("/api/professionals/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("PROFESSIONAL_NOT_FOUND"));
    }

    @Test
    void respondeProblemJsonSiElIdNoEsNumerico() throws Exception {
        mockMvc.perform(get("/api/professionals/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    void listaLosProfesionales() throws Exception {
        when(catalogService.getAllProfessionals()).thenReturn(List.of(
                Professional.builder().id(15L).categoryId(1L).firstName("Ana").lastName("Gomez").enabled(true).build()));

        mockMvc.perform(get("/api/professionals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Gomez"));
    }

    @Test
    void devuelveUnProfesionalPorId() throws Exception {
        when(catalogService.getProfessionalById(15L)).thenReturn(
                Professional.builder().id(15L).categoryId(1L).firstName("Ana").lastName("Gomez").enabled(true).build());

        mockMvc.perform(get("/api/professionals/15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(1));
    }

    @Test
    void unErrorInesperadoNoMuestraDetallesInternos() throws Exception {
        when(catalogService.getAllCategories()).thenThrow(new IllegalStateException("detalle interno"));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.detail").value("Ocurrió un error inesperado."));
    }
}
