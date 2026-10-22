package com.turnos.catalogo.catalog.application.service;

import com.turnos.catalogo.catalog.application.exception.ProfessionalNotFoundException;
import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.domain.model.ProfessionalCategory;
import com.turnos.catalogo.catalog.domain.model.WeeklySchedule;
import com.turnos.catalogo.catalog.domain.ports.in.GetAllCategoriesUseCase;
import com.turnos.catalogo.catalog.domain.ports.in.GetAllProfessionalsUseCase;
import com.turnos.catalogo.catalog.domain.ports.in.GetProfessionalByIdUseCase;
import com.turnos.catalogo.catalog.domain.ports.in.GetSchedulesByProfessionalUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogServiceTest {

    @Mock
    private GetAllCategoriesUseCase getAllCategoriesUseCase;

    @Mock
    private GetAllProfessionalsUseCase getAllProfessionalsUseCase;

    @Mock
    private GetProfessionalByIdUseCase getProfessionalByIdUseCase;

    @Mock
    private GetSchedulesByProfessionalUseCase getSchedulesByProfessionalUseCase;

    @InjectMocks
    private CatalogService service;

    @Test
    void lanzaExcepcionSiElProfesionalNoExiste() {
        when(getProfessionalByIdUseCase.getProfessionalById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getProfessionalById(99L))
                .isInstanceOf(ProfessionalNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void noConsultaAgendasDeUnProfesionalInexistente() {
        when(getProfessionalByIdUseCase.getProfessionalById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getSchedulesByProfessional(99L))
                .isInstanceOf(ProfessionalNotFoundException.class);
        verify(getSchedulesByProfessionalUseCase, never()).getSchedulesByProfessional(anyLong());
    }

    @Test
    void devuelveLasAgendasDeUnProfesionalExistente() {
        Professional ana = Professional.builder().id(15L).enabled(true).build();
        WeeklySchedule lunes = WeeklySchedule.builder().id(21L).professionalId(15L).build();
        when(getProfessionalByIdUseCase.getProfessionalById(15L)).thenReturn(Optional.of(ana));
        when(getSchedulesByProfessionalUseCase.getSchedulesByProfessional(15L)).thenReturn(List.of(lunes));

        assertThat(service.getSchedulesByProfessional(15L)).containsExactly(lunes);
    }

    @Test
    void delegaElListadoDeCategorias() {
        ProfessionalCategory clinica = ProfessionalCategory.builder().id(1L).enabled(true).build();
        when(getAllCategoriesUseCase.getAllCategories()).thenReturn(List.of(clinica));

        assertThat(service.getAllCategories()).containsExactly(clinica);
    }

    @Test
    void delegaElListadoDeProfesionales() {
        Professional ana = Professional.builder().id(15L).enabled(true).build();
        when(getAllProfessionalsUseCase.getAllProfessionals()).thenReturn(List.of(ana));

        assertThat(service.getAllProfessionals()).containsExactly(ana);
    }
}
