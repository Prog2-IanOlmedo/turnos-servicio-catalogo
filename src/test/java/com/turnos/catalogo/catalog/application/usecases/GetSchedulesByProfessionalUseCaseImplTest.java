package com.turnos.catalogo.catalog.application.usecases;

import com.turnos.catalogo.catalog.domain.model.WeeklySchedule;
import com.turnos.catalogo.catalog.domain.ports.out.WeeklyScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetSchedulesByProfessionalUseCaseImplTest {

    @Mock
    private WeeklyScheduleRepository repository;

    @InjectMocks
    private GetSchedulesByProfessionalUseCaseImpl useCase;

    @Test
    void devuelveSoloLasAgendasHabilitadasDelProfesional() {
        WeeklySchedule habilitada = WeeklySchedule.builder().id(21L).professionalId(15L).enabled(true).build();
        WeeklySchedule deshabilitada = WeeklySchedule.builder().id(22L).professionalId(15L).enabled(false).build();
        when(repository.findByProfessionalId(15L)).thenReturn(List.of(habilitada, deshabilitada));

        assertThat(useCase.getSchedulesByProfessional(15L)).containsExactly(habilitada);
    }
}
