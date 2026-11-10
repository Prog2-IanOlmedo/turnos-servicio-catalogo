package com.turnos.catalogo.catalog.application.exception;

// Redis no tiene algo que el contrato dice que debería estar (metadata o una entidad).
public class CatalogChangeUnavailableException extends RuntimeException {

    public CatalogChangeUnavailableException(String message) {
        super(message);
    }
}
