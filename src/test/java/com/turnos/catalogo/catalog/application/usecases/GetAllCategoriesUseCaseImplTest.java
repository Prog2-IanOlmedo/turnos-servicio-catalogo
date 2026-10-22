package com.turnos.catalogo.catalog.application.usecases;

import com.turnos.catalogo.catalog.domain.model.ProfessionalCategory;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalCategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllCategoriesUseCaseImplTest {

    @Mock
    private ProfessionalCategoryRepository repository;

    @InjectMocks
    private GetAllCategoriesUseCaseImpl useCase;

    @Test
    void devuelveSoloLasCategoriasHabilitadas() {
        ProfessionalCategory habilitada = ProfessionalCategory.builder().id(1L).enabled(true).build();
        ProfessionalCategory deshabilitada = ProfessionalCategory.builder().id(2L).enabled(false).build();
        when(repository.findAll()).thenReturn(List.of(habilitada, deshabilitada));

        assertThat(useCase.getAllCategories()).containsExactly(habilitada);
    }
}
