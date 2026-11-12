package com.turnos.catalogo.catalog.application.usecases;

import java.util.List;

import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.domain.model.ProfessionalFilter;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchProfessionalsUseCaseImplTest {

    @Mock
    private ProfessionalRepository repository;

    @InjectMocks
    private SearchProfessionalsUseCaseImpl useCase;

    private Professional profesional(Long id, Long categoria, String apellido, boolean habilitado) {
        return Professional.builder().id(id).categoryId(categoria).firstName("Nombre").lastName(apellido)
                .enabled(habilitado).build();
    }

    @Test
    void devuelveSoloLosQueCumplenElFiltro() {
        when(repository.findAll()).thenReturn(List.of(
                profesional(201L, 101L, "Rivas", true),
                profesional(202L, 102L, "Gomez", true),
                profesional(203L, 101L, "Paz", false)));

        assertThat(useCase.search(new ProfessionalFilter(101L, null)))
                .extracting(Professional::getId).containsExactly(201L);
        assertThat(useCase.search(ProfessionalFilter.none()))
                .extracting(Professional::getId).containsExactly(201L, 202L);
    }
}
