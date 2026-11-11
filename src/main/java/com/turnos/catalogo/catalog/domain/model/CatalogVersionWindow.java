package com.turnos.catalogo.catalog.domain.model;

// Versiones que la cátedra todavía conserva en Redis (contrato 14.2).
public record CatalogVersionWindow(long currentVersion, long oldestAvailableVersion) {

    public boolean isAheadOf(Long localVersion) {
        return localVersion == null || currentVersion > localVersion;
    }

    // Sin versión local, o con una anterior a la ventana, faltan incrementos y
    // hay que reconstruir con el snapshot (contrato 14.2 y 18.2).
    public boolean canContinueFrom(Long localVersion) {
        return localVersion != null && localVersion >= oldestAvailableVersion;
    }
}
