package com.turnos.catalogo.catalog.domain.ports.in;

import com.turnos.catalogo.catalog.domain.model.CatalogSnapshot;

public interface ApplyCatalogSnapshotUseCase {
    boolean applySnapshot(CatalogSnapshot snapshot);
}
