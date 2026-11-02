package com.turnos.catalogo.catalog.application.exception;

// La cátedra no respondió, respondió con error o mandó algo que no sirve como snapshot.
public class SnapshotUnavailableException extends RuntimeException {

    public SnapshotUnavailableException(String message) {
        super(message);
    }

    public SnapshotUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
