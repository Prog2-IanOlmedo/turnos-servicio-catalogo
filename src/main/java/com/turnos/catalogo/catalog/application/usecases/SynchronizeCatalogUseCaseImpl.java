package com.turnos.catalogo.catalog.application.usecases;

import com.turnos.catalogo.catalog.domain.ports.in.ApplyCatalogSnapshotUseCase;
import com.turnos.catalogo.catalog.domain.ports.in.SynchronizeCatalogUseCase;
import com.turnos.catalogo.catalog.domain.ports.out.CatalogSnapshotProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SynchronizeCatalogUseCaseImpl implements SynchronizeCatalogUseCase {

    private final CatalogSnapshotProvider snapshotProvider;
    private final ApplyCatalogSnapshotUseCase applyCatalogSnapshotUseCase;

    // Sin @Transactional a propósito: el pedido HTTP queda fuera de la transacción
    // y solo applySnapshot (que sí es transaccional) toca la base.
    // Devuelve false si la versión local ya estaba al día.
    @Override
    public boolean synchronize() {
        return applyCatalogSnapshotUseCase.applySnapshot(snapshotProvider.fetchSnapshot());
    }
}
