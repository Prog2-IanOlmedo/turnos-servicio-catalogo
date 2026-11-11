package com.turnos.catalogo.catalog.domain.ports.in;

import com.turnos.catalogo.catalog.domain.model.SyncResult;

public interface CatchUpCatalogUseCase {
    SyncResult catchUp();
}
