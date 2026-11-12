package com.turnos.catalogo.catalog.domain.model;

import java.time.Instant;
import java.util.Locale;
import java.util.Objects;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Professional {
    private Long id;
    private Long categoryId;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;

    public boolean belongsToCategory(Long id) {
        return categoryId != null && categoryId.equals(id);
    }

    // Busca en nombre y apellido juntos, sin importar mayúsculas.
    public boolean nameContains(String text) {
        String fullName = Objects.toString(firstName, "") + " " + Objects.toString(lastName, "");
        return fullName.toLowerCase(Locale.ROOT).contains(text.trim().toLowerCase(Locale.ROOT));
    }
}
