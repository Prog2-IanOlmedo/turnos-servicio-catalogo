package com.turnos.catalogo.catalog.application.exception;

// Se quiso aplicar una versión que no es la siguiente a la local.
public class CatalogVersionGapException extends RuntimeException {

    public CatalogVersionGapException(Long localVersion, long version) {
        super("No se puede aplicar la versión " + version + " sobre la versión local " + localVersion);
    }
}
