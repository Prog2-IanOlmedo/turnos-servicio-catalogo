package com.turnos.catalogo.catalog.application.usecases;

import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetProfessionalByIdUseCaseImplTest {

    @Mock
    private ProfessionalRepository repository;

    @InjectMocks
    private GetProfessionalByIdUseCaseImpl useCase;

    @Test
    void devuelveElProfesionalSiEstaHabilitado() {
        Professional ana = Professional.builder().id(15L).enabled(true).build();
        when(repository.findById(15L)).thenReturn(Optional.of(ana));

        assertThat(useCase.getProfessionalById(15L)).contains(ana);
    }

    @Test
    void noDevuelveUnProfesionalDeshabilitado() {
        when(repository.findById(16L))
                .thenReturn(Optional.of(Professional.builder().id(16L).enabled(false).build()));

        assertThat(useCase.getProfessionalById(16L)).isEmpty();
    }
}
