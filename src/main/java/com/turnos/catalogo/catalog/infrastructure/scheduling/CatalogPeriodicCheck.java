package com.turnos.catalogo.catalog.infrastructure.scheduling;

import com.turnos.catalogo.catalog.application.service.CatalogService;
import com.turnos.catalogo.catalog.domain.model.SyncResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// Si se pierde un aviso de Kafka, comparar la versión local con la de Redis lo detecta
// igual (sección 16). Por defecto cada 5 minutos.
@Slf4j
@Component
public class CatalogPeriodicCheck {

    private final CatalogService catalogService;
    private final boolean enabled;

    public CatalogPeriodicCheck(CatalogService catalogService,
                                @Value("${catedra.sync.periodic.enabled:true}") boolean enabled) {
        this.catalogService = catalogService;
        this.enabled = enabled;
    }

    @Scheduled(fixedDelayString = "${catedra.sync.periodic.interval-ms:300000}",
            initialDelayString = "${catedra.sync.periodic.interval-ms:300000}")
    public void check() {
        if (!enabled) {
            return;
        }
        try {
            SyncResult result = catalogService.catchUpCatalog();
            if (result != SyncResult.UP_TO_DATE) {
                log.info("Chequeo periódico del catálogo: {}", result);
            }
        } catch (RuntimeException e) {
            log.warn("Falló el chequeo periódico del catálogo: {}", e.getMessage());
        }
    }
}
