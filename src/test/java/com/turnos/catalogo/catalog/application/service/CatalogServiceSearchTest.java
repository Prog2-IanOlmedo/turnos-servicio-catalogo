package com.turnos.catalogo.catalog.application.service;

import java.util.List;

import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.domain.model.ProfessionalFilter;
import com.turnos.catalogo.catalog.domain.ports.in.SearchProfessionalsUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogServiceSearchTest {

    @Mock
    private SearchProfessionalsUseCase searchProfessionalsUseCase;

    @InjectMocks
    private CatalogService service;

    @Test
    void delegaLaBusqueda() {
        ProfessionalFilter filtro = new ProfessionalFilter(101L, "riv");
        Professional rivas = Professional.builder().id(202L).build();
        when(searchProfessionalsUseCase.search(filtro)).thenReturn(List.of(rivas));

        assertThat(service.searchProfessionals(filtro)).containsExactly(rivas);
    }
}
