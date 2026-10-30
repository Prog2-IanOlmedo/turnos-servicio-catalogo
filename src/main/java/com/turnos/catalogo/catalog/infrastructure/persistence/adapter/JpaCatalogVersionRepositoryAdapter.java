package com.turnos.catalogo.catalog.infrastructure.persistence.adapter;

import com.turnos.catalogo.catalog.domain.ports.out.CatalogVersionRepository;
import com.turnos.catalogo.catalog.infrastructure.persistence.entity.CatalogSyncStateEntity;
import com.turnos.catalogo.catalog.infrastructure.persistence.repository.JpaCatalogSyncStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaCatalogVersionRepositoryAdapter implements CatalogVersionRepository {

    private final JpaCatalogSyncStateRepository jpaRepository;

    @Override
    public Optional<Long> findCurrentVersion() {
        return jpaRepository.findById(CatalogSyncStateEntity.SINGLE_ROW_ID)
                .map(CatalogSyncStateEntity::getCatalogVersion);
    }

    @Override
    public void saveVersion(long version) {
        jpaRepository.save(CatalogSyncStateEntity.builder()
                .id(CatalogSyncStateEntity.SINGLE_ROW_ID)
                .catalogVersion(version)
                .updatedAt(Instant.now())
                .build());
    }
}
