package com.turnos.catalogo.catalog.application.usecases;

import java.util.List;
import java.util.Optional;

import com.turnos.catalogo.catalog.application.exception.CatalogVersionGapException;
import com.turnos.catalogo.catalog.domain.model.CatalogChange;
import com.turnos.catalogo.catalog.domain.model.CatalogVersionWindow;
import com.turnos.catalogo.catalog.domain.model.SyncResult;
import com.turnos.catalogo.catalog.domain.ports.in.ApplyCatalogChangeUseCase;
import com.turnos.catalogo.catalog.domain.ports.in.SynchronizeCatalogUseCase;
import com.turnos.catalogo.catalog.domain.ports.out.CatalogChangeSource;
import com.turnos.catalogo.catalog.domain.ports.out.CatalogVersionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatchUpCatalogUseCaseImplTest {

    private static final CatalogVersionWindow VENTANA = new CatalogVersionWindow(148, 49);

    @Mock
    private CatalogChangeSource changeSource;

    @Mock
    private CatalogVersionRepository versionRepository;

    @Mock
    private ApplyCatalogChangeUseCase applyCatalogChangeUseCase;

    @Mock
    private SynchronizeCatalogUseCase synchronizeCatalogUseCase;

    @InjectMocks
    private CatchUpCatalogUseCaseImpl useCase;

    private CatalogChange cambio(long version) {
        return new CatalogChange(version, List.of(), List.of(), List.of());
    }

    private void versionLocal(Long version) {
        when(versionRepository.findCurrentVersion()).thenReturn(Optional.ofNullable(version));
        when(changeSource.currentWindow()).thenReturn(VENTANA);
    }

    @Test
    void siYaEstaAlDiaNoHaceNada() {
        versionLocal(148L);

        assertThat(useCase.catchUp()).isEqualTo(SyncResult.UP_TO_DATE);
        verifyNoInteractions(applyCatalogChangeUseCase, synchronizeCatalogUseCase);
    }

    @Test
    void sinVersionLocalReconstruyeConElSnapshot() {
        versionLocal(null);

        assertThat(useCase.catchUp()).isEqualTo(SyncResult.SNAPSHOT);
        verify(synchronizeCatalogUseCase).synchronize();
    }

    @Test
    void unaVersionLocalFueraDeLaVentanaReconstruyeConElSnapshot() {
        versionLocal(1L);

        assertThat(useCase.catchUp()).isEqualTo(SyncResult.SNAPSHOT);
        verify(synchronizeCatalogUseCase).synchronize();
        verify(changeSource, never()).findChange(anyLong());
    }

    @Test
    void aplicaLasVersionesPendientesEnOrden() {
        versionLocal(146L);
        when(changeSource.findChange(147)).thenReturn(Optional.of(cambio(147)));
        when(changeSource.findChange(148)).thenReturn(Optional.of(cambio(148)));

        assertThat(useCase.catchUp()).isEqualTo(SyncResult.INCREMENTAL);
        InOrder orden = inOrder(applyCatalogChangeUseCase);
        orden.verify(applyCatalogChangeUseCase).applyChange(cambio(147));
        orden.verify(applyCatalogChangeUseCase).applyChange(cambio(148));
        verify(synchronizeCatalogUseCase, never()).synchronize();
    }

    @Test
    void unHuecoEnElHistorialReconstruyeConElSnapshot() {
        versionLocal(146L);
        when(changeSource.findChange(147)).thenReturn(Optional.empty());

        assertThat(useCase.catchUp()).isEqualTo(SyncResult.SNAPSHOT);
        verify(applyCatalogChangeUseCase, never()).applyChange(any());
        verify(synchronizeCatalogUseCase).synchronize();
    }

    @Test
    void siUnaVersionNoSePuedeAplicarReconstruyeConElSnapshot() {
        versionLocal(147L);
        when(changeSource.findChange(148)).thenReturn(Optional.of(cambio(148)));
        doThrow(new CatalogVersionGapException(147L, 148)).when(applyCatalogChangeUseCase).applyChange(any());

        assertThat(useCase.catchUp()).isEqualTo(SyncResult.SNAPSHOT);
        verify(synchronizeCatalogUseCase).synchronize();
    }
}
