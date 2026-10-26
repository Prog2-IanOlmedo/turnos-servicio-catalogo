package com.turnos.catalogo.catalog.application.exception;

public class InvalidSnapshotException extends RuntimeException {

    private final long snapshotVersion;

    public InvalidSnapshotException(long snapshotVersion) {
        super("El snapshot " + snapshotVersion + " tiene referencias inconsistentes");
        this.snapshotVersion = snapshotVersion;
    }

    public long getSnapshotVersion() {
        return snapshotVersion;
    }
}
