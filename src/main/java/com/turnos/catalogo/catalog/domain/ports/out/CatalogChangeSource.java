package com.turnos.catalogo.catalog.domain.ports.out;

import java.util.Optional;

import com.turnos.catalogo.catalog.domain.model.CatalogChange;
import com.turnos.catalogo.catalog.domain.model.CatalogVersionWindow;

// De dónde salen los cambios incrementales. Hoy es el Redis de la cátedra.
public interface CatalogChangeSource {

    CatalogVersionWindow currentWindow();

    // Vacío si esa versión ya no está disponible.
    Optional<CatalogChange> findChange(long version);
}
