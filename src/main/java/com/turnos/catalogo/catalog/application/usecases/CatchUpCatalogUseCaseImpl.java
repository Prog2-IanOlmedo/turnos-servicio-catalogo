package com.turnos.catalogo.catalog.application.usecases;

import java.util.Optional;

import com.turnos.catalogo.catalog.domain.model.CatalogChange;
import com.turnos.catalogo.catalog.domain.model.CatalogVersionWindow;
import com.turnos.catalogo.catalog.domain.model.SyncResult;
import com.turnos.catalogo.catalog.domain.ports.in.ApplyCatalogChangeUseCase;
import com.turnos.catalogo.catalog.domain.ports.in.CatchUpCatalogUseCase;
import com.turnos.catalogo.catalog.domain.ports.in.SynchronizeCatalogUseCase;
import com.turnos.catalogo.catalog.domain.ports.out.CatalogChangeSource;
import com.turnos.catalogo.catalog.domain.ports.out.CatalogVersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// Diagrama de la sección 16: con historial continuo aplico versión por versión;
// con un hueco, una versión local fuera de la ventana o un cambio que no se puede
// aplicar, reconstruyo con el snapshot. synchronized porque lo pueden disparar a la
// vez Kafka, el chequeo periódico y el arranque.
@Slf4j
@Component
@RequiredArgsConstructor
public class CatchUpCatalogUseCaseImpl implements CatchUpCatalogUseCase {

    private final CatalogChangeSource changeSource;
    private final CatalogVersionRepository versionRepository;
    private final ApplyCatalogChangeUseCase applyCatalogChangeUseCase;
    private final SynchronizeCatalogUseCase synchronizeCatalogUseCase;

    @Override
    public synchronized SyncResult catchUp() {
        Long localVersion = versionRepository.findCurrentVersion().orElse(null);
        CatalogVersionWindow window = changeSource.currentWindow();
        if (!window.isAheadOf(localVersion)) {
            return SyncResult.UP_TO_DATE;
        }
        if (localVersion == null || !window.canContinueFrom(localVersion)) {
            return rebuild("la versión local " + localVersion + " no alcanza la ventana " + window);
        }
        for (long version = localVersion + 1; version <= window.currentVersion(); version++) {
            Optional<CatalogChange> change = changeSource.findChange(version);
            if (change.isEmpty()) {
                return rebuild("no está disponible changes:" + version);
            }
            try {
                applyCatalogChangeUseCase.applyChange(change.get());
            } catch (RuntimeException e) {
                return rebuild("no se pudo aplicar la versión " + version + ": " + e.getMessage());
            }
        }
        log.info("Catálogo actualizado por incrementos hasta la versión {}", window.currentVersion());
        return SyncResult.INCREMENTAL;
    }

    private SyncResult rebuild(String reason) {
        log.warn("No se puede seguir por incrementos ({}): reconstruyo con el snapshot", reason);
        synchronizeCatalogUseCase.synchronize();
        return SyncResult.SNAPSHOT;
    }
}
