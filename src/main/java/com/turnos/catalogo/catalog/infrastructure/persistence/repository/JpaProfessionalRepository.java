package com.turnos.catalogo.catalog.infrastructure.persistence.repository;

import com.turnos.catalogo.catalog.infrastructure.persistence.entity.ProfessionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProfessionalRepository extends JpaRepository<ProfessionalEntity, Long> {
}
