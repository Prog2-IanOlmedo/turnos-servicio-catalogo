package com.turnos.catalogo.catalog.application.usecases;

import com.turnos.catalogo.catalog.application.exception.InvalidSnapshotException;
import com.turnos.catalogo.catalog.domain.model.CatalogSnapshot;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplyCatalogSnapshotUseCaseImplTest {

    @Mock
    private ProfessionalCategoryRepository categoryRepository;

    @Mock
    private ProfessionalRepository professionalRepository;

    @Mock
    private WeeklyScheduleRepository scheduleRepository;

    @Mock
    private CatalogVersionRepository versionRepository;

    @InjectMocks
    private ApplyCatalogSnapshotUseCaseImpl useCase;

    private final ProfessionalCategory clinica = ProfessionalCategory.builder().id(1L).build();
    private final Professional ana = Professional.builder().id(15L).categoryId(1L).build();
    private final WeeklySchedule lunes = WeeklySchedule.builder().id(21L).professionalId(15L).build();

    private CatalogSnapshot snapshot(long version, List<Professional> profesionales) {
        return CatalogSnapshot.builder().snapshotVersion(version)
                .categories(List.of(clinica)).professionals(profesionales).schedules(List.of(lunes)).build();
    }

    @Test
    void guardaEnElOrdenDeLasClavesForaneasYRegistraLaVersion() {
        when(versionRepository.findCurrentVersion()).thenReturn(Optional.of(6L));

        assertThat(useCase.applySnapshot(snapshot(7, List.of(ana)))).isTrue();

        InOrder orden = inOrder(categoryRepository, professionalRepository, scheduleRepository, versionRepository);
        orden.verify(categoryRepository).save(clinica);
        orden.verify(professionalRepository).save(ana);
        orden.verify(scheduleRepository).save(lunes);
        orden.verify(versionRepository).saveVersion(7);
    }

    @Test
    void ignoraUnSnapshotQueNoEsMasNuevo() {
        when(versionRepository.findCurrentVersion()).thenReturn(Optional.of(7L));

        assertThat(useCase.applySnapshot(snapshot(7, List.of(ana)))).isFalse();
        verify(categoryRepository, never()).save(any());
        verify(versionRepository, never()).saveVersion(anyLong());
    }

    @Test
    void rechazaUnSnapshotIncoherenteSinGuardarNada() {
        when(versionRepository.findCurrentVersion()).thenReturn(Optional.empty());
        Professional sinCategoria = Professional.builder().id(16L).categoryId(99L).build();

        assertThatThrownBy(() -> useCase.applySnapshot(snapshot(1, List.of(sinCategoria))))
                .isInstanceOf(InvalidSnapshotException.class);
        verify(categoryRepository, never()).save(any());
        verify(versionRepository, never()).saveVersion(anyLong());
    }
}
