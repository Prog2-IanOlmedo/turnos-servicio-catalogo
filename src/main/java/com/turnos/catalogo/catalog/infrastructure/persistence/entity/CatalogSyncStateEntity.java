package com.turnos.catalogo.catalog.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

// Una sola fila (id = 1), como fija la migración V1.
@Getter
@Setter
@Entity
@Table(name = "catalog_sync_state")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogSyncStateEntity {
    public static final short SINGLE_ROW_ID = 1;

    @Id
    private Short id;
    private Long catalogVersion;
    private Instant updatedAt;
}
