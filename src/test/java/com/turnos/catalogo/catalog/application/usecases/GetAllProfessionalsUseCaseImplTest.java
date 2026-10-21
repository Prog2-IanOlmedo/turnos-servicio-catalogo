package com.turnos.catalogo.catalog.application.usecases;

import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllProfessionalsUseCaseImplTest {

    @Mock
    private ProfessionalRepository repository;

    @InjectMocks
    private GetAllProfessionalsUseCaseImpl useCase;

    @Test
    void devuelveSoloLosProfesionalesHabilitados() {
        Professional habilitado = Professional.builder().id(15L).enabled(true).build();
        Professional deshabilitado = Professional.builder().id(16L).enabled(false).build();
        when(repository.findAll()).thenReturn(List.of(habilitado, deshabilitado));

        assertThat(useCase.getAllProfessionals()).containsExactly(habilitado);
    }
}
