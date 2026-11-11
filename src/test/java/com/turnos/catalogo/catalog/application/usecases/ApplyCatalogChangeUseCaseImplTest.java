package com.turnos.catalogo.catalog.application.usecases;

import java.util.List;
import java.util.Optional;

import com.turnos.catalogo.catalog.application.exception.CatalogVersionGapException;
import com.turnos.catalogo.catalog.domain.model.CatalogChange;
import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.domain.model.ProfessionalCategory;
import com.turnos.catalogo.catalog.domain.model.WeeklySchedule;
import com.turnos.catalogo.catalog.domain.ports.out.CatalogVersionRepository;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalCategoryRepository;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalRepository;
import com.turnos.catalogo.catalog.domain.ports.out.WeeklyScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplyCatalogChangeUseCaseImplTest {

    @Mock
    private ProfessionalCategoryRepository categoryRepository;

    @Mock
    private ProfessionalRepository professionalRepository;

    @Mock
    private WeeklyScheduleRepository scheduleRepository;

    @Mock
    private CatalogVersionRepository versionRepository;

    @InjectMocks
    private ApplyCatalogChangeUseCaseImpl useCase;

    private final ProfessionalCategory categoria = ProfessionalCategory.builder().id(101L).enabled(true).build();
    private final Professional profesional = Professional.builder().id(201L).categoryId(101L).enabled(false).build();
    private final WeeklySchedule agenda = WeeklySchedule.builder().id(301L).professionalId(201L).build();

    @Test
    void guardaEnElOrdenDeLasClavesYRecienAlFinalAvanzaLaVersion() {
        when(versionRepository.findCurrentVersion()).thenReturn(Optional.of(147L));

        boolean aplicado = useCase.applyChange(
                new CatalogChange(148, List.of(categoria), List.of(profesional), List.of(agenda)));

        assertThat(aplicado).isTrue();
        InOrder orden = inOrder(categoryRepository, professionalRepository, scheduleRepository, versionRepository);
        orden.verify(categoryRepository).save(categoria);
        orden.verify(professionalRepository).save(profesional);
        orden.verify(scheduleRepository).save(agenda);
        orden.verify(versionRepository).saveVersion(148L);
    }

    @Test
    void unaVersionYaAplicadaNoSeRepite() {
        when(versionRepository.findCurrentVersion()).thenReturn(Optional.of(148L));

        assertThat(useCase.applyChange(new CatalogChange(148, List.of(), List.of(profesional), List.of())))
                .isFalse();
        verify(professionalRepository, never()).save(any());
        verify(versionRepository, never()).saveVersion(anyLong());
    }

    @Test
    void noSeSaltaUnaVersion() {
        when(versionRepository.findCurrentVersion()).thenReturn(Optional.of(146L));
        CatalogChange cambio = new CatalogChange(148, List.of(), List.of(profesional), List.of());

        assertThatThrownBy(() -> useCase.applyChange(cambio))
                .isInstanceOf(CatalogVersionGapException.class)
                .hasMessageContaining("148");
        verify(versionRepository, never()).saveVersion(anyLong());
    }

    @Test
    void sinVersionLocalNoSeAplicanIncrementos() {
        when(versionRepository.findCurrentVersion()).thenReturn(Optional.empty());
        CatalogChange cambio = new CatalogChange(148, List.of(), List.of(), List.of());

        assertThatThrownBy(() -> useCase.applyChange(cambio)).isInstanceOf(CatalogVersionGapException.class);
    }
}
