package com.turnos.catalogo.catalog.infrastructure.persistence.mapper;

import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.infrastructure.persistence.entity.ProfessionalEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfessionalMapper {

    public ProfessionalEntity toEntity(Professional professional) {
        if (professional == null) {
            return null;
        }
        return ProfessionalEntity.builder()
                .id(professional.getId())
                .categoryId(professional.getCategoryId())
                .firstName(professional.getFirstName())
                .lastName(professional.getLastName())
                .enabled(professional.isEnabled())
                .createdAt(professional.getCreatedAt())
                .updatedAt(professional.getUpdatedAt())
                .build();
    }

    public Professional toDomainModel(ProfessionalEntity entity) {
        if (entity == null) {
            return null;
        }
        return Professional.builder()
                .id(entity.getId())
                .categoryId(entity.getCategoryId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .enabled(entity.isEnabled())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
