package com.turnos.catalogo.catalog.domain.ports.in;

import com.turnos.catalogo.catalog.domain.model.Professional;

import java.util.Optional;

public interface GetProfessionalByIdUseCase {
    Optional<Professional> getProfessionalById(Long id);
}
