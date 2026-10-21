package com.turnos.catalogo.catalog.domain.ports.in;

import com.turnos.catalogo.catalog.domain.model.Professional;

import java.util.List;

public interface GetAllProfessionalsUseCase {
    List<Professional> getAllProfessionals();
}
