package com.turnos.catalogo.catalog.application.usecases;

import com.turnos.catalogo.catalog.application.exception.SnapshotUnavailableException;
import com.turnos.catalogo.catalog.domain.model.CatalogSnapshot;
import com.turnos.catalogo.catalog.domain.ports.in.ApplyCatalogSnapshotUseCase;
import com.turnos.catalogo.catalog.domain.ports.out.CatalogSnapshotProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SynchronizeCatalogUseCaseImplTest {

    @Mock
    private CatalogSnapshotProvider snapshotProvider;

    @Mock
    private ApplyCatalogSnapshotUseCase applyCatalogSnapshotUseCase;

    @InjectMocks
    private SynchronizeCatalogUseCaseImpl useCase;

    @Test
    void aplicaElSnapshotQueTraeDeLaCatedra() {
        CatalogSnapshot snapshot = CatalogSnapshot.builder().snapshotVersion(1L).build();
        when(snapshotProvider.fetchSnapshot()).thenReturn(snapshot);
        when(applyCatalogSnapshotUseCase.applySnapshot(snapshot)).thenReturn(true);

        assertThat(useCase.synchronize()).isTrue();
        verify(applyCatalogSnapshotUseCase).applySnapshot(snapshot);
    }

    @Test
    void noAplicaNadaSiLaCatedraNoResponde() {
        when(snapshotProvider.fetchSnapshot()).thenThrow(new SnapshotUnavailableException("sin respuesta"));

        assertThatThrownBy(useCase::synchronize).isInstanceOf(SnapshotUnavailableException.class);
        verify(applyCatalogSnapshotUseCase, never()).applySnapshot(any());
    }
}
