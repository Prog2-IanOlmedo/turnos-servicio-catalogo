package com.turnos.catalogo.catalog.infrastructure.persistence.repository;

import com.turnos.catalogo.catalog.infrastructure.persistence.entity.ProfessionalCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProfessionalCategoryRepository extends JpaRepository<ProfessionalCategoryEntity, Long> {
}
