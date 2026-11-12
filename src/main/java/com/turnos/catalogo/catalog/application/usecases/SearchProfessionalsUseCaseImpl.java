package com.turnos.catalogo.catalog.application.usecases;

import java.util.List;

import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.domain.model.ProfessionalFilter;
import com.turnos.catalogo.catalog.domain.ports.in.SearchProfessionalsUseCase;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// El catálogo es chico (lo publica la cátedra), así que filtro en memoria con la
// regla del dominio en vez de armar una consulta por cada combinación.
@Component
@RequiredArgsConstructor
public class SearchProfessionalsUseCaseImpl implements SearchProfessionalsUseCase {

    private final ProfessionalRepository repository;

    @Override
    public List<Professional> search(ProfessionalFilter filter) {
        return repository.findAll().stream()
                .filter(filter::accepts)
                .toList();
    }
}
