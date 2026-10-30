package com.turnos.catalogo.catalog.application.usecases;

import com.turnos.catalogo.catalog.application.exception.InvalidSnapshotException;
import com.turnos.catalogo.catalog.domain.model.CatalogSnapshot;
import com.turnos.catalogo.catalog.domain.ports.in.ApplyCatalogSnapshotUseCase;
import com.turnos.catalogo.catalog.domain.ports.out.CatalogVersionRepository;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalCategoryRepository;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalRepository;
import com.turnos.catalogo.catalog.domain.ports.out.WeeklyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ApplyCatalogSnapshotUseCaseImpl implements ApplyCatalogSnapshotUseCase {

    private final ProfessionalCategoryRepository categoryRepository;
    private final ProfessionalRepository professionalRepository;
    private final WeeklyScheduleRepository scheduleRepository;
    private final CatalogVersionRepository versionRepository;

    // Todo en una transacción: la versión se guarda solo si las tres
    // colecciones quedaron aplicadas (contrato, sección 7).
    @Override
    @Transactional
    public boolean applySnapshot(CatalogSnapshot snapshot) {
        if (!snapshot.isNewerThan(versionRepository.findCurrentVersion().orElse(null))) {
            return false;
        }
        if (!snapshot.hasConsistentReferences()) {
            throw new InvalidSnapshotException(snapshot.getSnapshotVersion());
        }
        // Orden de las claves foráneas: categorías, profesionales, agendas.
        snapshot.getCategories().forEach(categoryRepository::save);
        snapshot.getProfessionals().forEach(professionalRepository::save);
        snapshot.getSchedules().forEach(scheduleRepository::save);
        versionRepository.saveVersion(snapshot.getSnapshotVersion());
        return true;
    }
}
