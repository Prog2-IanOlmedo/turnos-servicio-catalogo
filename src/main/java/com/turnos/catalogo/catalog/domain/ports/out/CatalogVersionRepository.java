package com.turnos.catalogo.catalog.domain.ports.out;

import java.util.Optional;

public interface CatalogVersionRepository {

    Optional<Long> findCurrentVersion();
    void saveVersion(long version);

}
