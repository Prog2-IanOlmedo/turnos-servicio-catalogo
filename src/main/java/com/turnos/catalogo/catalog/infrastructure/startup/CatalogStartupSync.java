package com.turnos.catalogo.catalog.infrastructure.startup;

import com.turnos.catalogo.catalog.application.service.CatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

// Al arrancar pide el snapshot completo a la cátedra (contrato, sección 7).
// Si la cátedra no responde, la app levanta igual con lo que ya tiene en la base.
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
            if (catalogService.synchronizeCatalog()) {
                log.info("Catálogo sincronizado con la cátedra");
            } else {
                log.info("El catálogo local ya estaba al día");
            }
        } catch (RuntimeException e) {
            log.warn("No se pudo sincronizar el catálogo al arrancar: {}", e.getMessage());
        }
    }
}
