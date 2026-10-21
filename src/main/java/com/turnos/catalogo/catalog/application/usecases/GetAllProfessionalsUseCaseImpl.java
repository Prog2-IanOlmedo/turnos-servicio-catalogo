package com.turnos.catalogo.catalog.application.usecases;

import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.domain.ports.in.GetAllProfessionalsUseCase;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllProfessionalsUseCaseImpl implements GetAllProfessionalsUseCase {

    private final ProfessionalRepository repository;

    @Override
    public List<Professional> getAllProfessionals() {
        return repository.findAll().stream()
                .filter(Professional::isEnabled)
                .toList();
    }
}
