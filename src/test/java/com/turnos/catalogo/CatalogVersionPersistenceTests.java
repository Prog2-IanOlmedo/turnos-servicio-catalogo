package com.turnos.catalogo;

import com.turnos.catalogo.catalog.domain.ports.out.CatalogVersionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CatalogVersionPersistenceTests {

    @Autowired
    private CatalogVersionRepository repository;

    @Test
    void guardaYActualizaLaUnicaFilaDeVersion() {
        repository.saveVersion(100);
        assertThat(repository.findCurrentVersion()).contains(100L);

        repository.saveVersion(101);
        assertThat(repository.findCurrentVersion()).contains(101L);
    }
}
