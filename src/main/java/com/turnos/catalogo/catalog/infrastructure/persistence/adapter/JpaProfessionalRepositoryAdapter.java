package com.turnos.catalogo.catalog.infrastructure.persistence.adapter;

import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalRepository;
import com.turnos.catalogo.catalog.infrastructure.persistence.mapper.ProfessionalMapper;
import com.turnos.catalogo.catalog.infrastructure.persistence.repository.JpaProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaProfessionalRepositoryAdapter implements ProfessionalRepository {

    private final JpaProfessionalRepository jpaRepository;
    private final ProfessionalMapper mapper;

    @Override
    public Professional save(Professional professional) {
        return mapper.toDomainModel(jpaRepository.save(mapper.toEntity(professional)));
    }

    @Override
    public Optional<Professional> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomainModel);
    }

    @Override
    public List<Professional> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomainModel)
                .toList();
    }
}
