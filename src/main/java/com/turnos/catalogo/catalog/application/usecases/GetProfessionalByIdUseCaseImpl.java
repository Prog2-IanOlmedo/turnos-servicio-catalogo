package com.turnos.catalogo.catalog.application.usecases;

import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.domain.ports.in.GetProfessionalByIdUseCase;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetProfessionalByIdUseCaseImpl implements GetProfessionalByIdUseCase {

    private final ProfessionalRepository repository;

    @Override
    public Optional<Professional> getProfessionalById(Long id) {
        return repository.findById(id).filter(Professional::isEnabled);
    }
}
