package com.turnos.catalogo.catalog.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "professional_category")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalCategoryEntity {
    // Sin @GeneratedValue: el id viene de la cátedra.
    @Id
    private Long id;
    private String name;
    private String description;
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;
}
