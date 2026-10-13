package com.turnos.catalogo.catalog.infrastructure.persistence.mapper;

import com.turnos.catalogo.catalog.domain.model.ProfessionalCategory;
import com.turnos.catalogo.catalog.infrastructure.persistence.entity.ProfessionalCategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfessionalCategoryMapper {

    public ProfessionalCategoryEntity toEntity(ProfessionalCategory category) {
        if (category == null) {
            return null;
        }
        return ProfessionalCategoryEntity.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .enabled(category.isEnabled())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    public ProfessionalCategory toDomainModel(ProfessionalCategoryEntity entity) {
        if (entity == null) {
            return null;
        }
        return ProfessionalCategory.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .enabled(entity.isEnabled())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
