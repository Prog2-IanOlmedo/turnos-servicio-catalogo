package com.turnos.catalogo.catalog.domain.model;

// Cómo terminó una sincronización: no hacía falta, por incrementos o con el snapshot.
public enum SyncResult {
    UP_TO_DATE,
    INCREMENTAL,
    SNAPSHOT
}
