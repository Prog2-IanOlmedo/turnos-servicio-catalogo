package com.turnos.catalogo.catalog.domain.ports.in;

import java.util.List;

import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.domain.model.ProfessionalFilter;

public interface SearchProfessionalsUseCase {
    List<Professional> search(ProfessionalFilter filter);
}
