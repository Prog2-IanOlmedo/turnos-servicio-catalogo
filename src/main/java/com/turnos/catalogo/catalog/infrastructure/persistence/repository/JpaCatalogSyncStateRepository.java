package com.turnos.catalogo.catalog.infrastructure.persistence.repository;

import com.turnos.catalogo.catalog.infrastructure.persistence.entity.CatalogSyncStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCatalogSyncStateRepository extends JpaRepository<CatalogSyncStateEntity, Short> {
}
