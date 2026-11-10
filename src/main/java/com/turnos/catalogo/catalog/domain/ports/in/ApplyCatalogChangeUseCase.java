package com.turnos.catalogo.catalog.domain.ports.in;

import com.turnos.catalogo.catalog.domain.model.CatalogChange;

public interface ApplyCatalogChangeUseCase {
    boolean applyChange(CatalogChange change);
}
