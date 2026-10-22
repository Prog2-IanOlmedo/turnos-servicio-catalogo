package com.turnos.catalogo.catalog.application.exception;

public class ProfessionalNotFoundException extends RuntimeException {

    private final Long professionalId;

    public ProfessionalNotFoundException(Long professionalId) {
        super("No existe un profesional habilitado con id: " + professionalId);
        this.professionalId = professionalId;
    }

    public Long getProfessionalId() {
        return professionalId;
    }
}
