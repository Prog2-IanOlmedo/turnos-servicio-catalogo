package com.turnos.catalogo.catalog.infrastructure.persistence.adapter;

import com.turnos.catalogo.catalog.domain.model.ProfessionalCategory;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalCategoryRepository;
import com.turnos.catalogo.catalog.infrastructure.persistence.mapper.ProfessionalCategoryMapper;
import com.turnos.catalogo.catalog.infrastructure.persistence.repository.JpaProfessionalCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaProfessionalCategoryRepositoryAdapter implements ProfessionalCategoryRepository {

    private final JpaProfessionalCategoryRepository jpaRepository;
    private final ProfessionalCategoryMapper mapper;

    @Override
    public ProfessionalCategory save(ProfessionalCategory category) {
        return mapper.toDomainModel(jpaRepository.save(mapper.toEntity(category)));
    }

    @Override
    public Optional<ProfessionalCategory> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomainModel);
    }

    @Override
    public List<ProfessionalCategory> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomainModel)
                .toList();
    }
}
