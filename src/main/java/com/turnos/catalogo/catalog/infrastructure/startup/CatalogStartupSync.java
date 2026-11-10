package com.turnos.catalogo.catalog.infrastructure.startup;

import com.turnos.catalogo.catalog.application.service.CatalogService;
import com.turnos.catalogo.catalog.domain.model.SyncResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

// Al arrancar se pone al día con la cátedra: con la base vacía o muy atrasada termina
// en snapshot (sección 7), si no aplica los incrementos pendientes. Si la cátedra no
// responde, la app levanta igual con lo que ya tiene en la base.
@Slf4j
@Component
public class CatalogStartupSync implements ApplicationRunner {

    private final CatalogService catalogService;
    private final boolean enabled;

    public CatalogStartupSync(CatalogService catalogService,
                              @Value("${catedra.sync.on-startup:true}") boolean enabled) {
        this.catalogService = catalogService;
        this.enabled = enabled;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!enabled) {
            log.info("Sincronización del catálogo al arrancar deshabilitada");
            return;
        }
        try {
            SyncResult result = catalogService.catchUpCatalog();
            log.info("Sincronización del catálogo al arrancar: {}", result);
        } catch (RuntimeException e) {
            log.warn("No se pudo sincronizar el catálogo al arrancar: {}", e.getMessage());
        }
    }
}
