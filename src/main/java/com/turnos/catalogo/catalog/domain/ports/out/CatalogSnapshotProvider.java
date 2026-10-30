package com.turnos.catalogo.catalog.domain.ports.out;

import com.turnos.catalogo.catalog.domain.model.CatalogSnapshot;

// De dónde sale el snapshot completo. Hoy es la API REST de la cátedra,
// pero el dominio no se entera de eso.
public interface CatalogSnapshotProvider {

    CatalogSnapshot fetchSnapshot();
}
