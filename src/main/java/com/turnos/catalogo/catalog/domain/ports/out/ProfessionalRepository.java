package com.turnos.catalogo.catalog.domain.ports.out;

import com.turnos.catalogo.catalog.domain.model.Professional;

import java.util.List;
import java.util.Optional;

public interface ProfessionalRepository {

    Professional save(Professional professional);
    Optional<Professional> findById(Long id);
    List<Professional> findAll();

}
