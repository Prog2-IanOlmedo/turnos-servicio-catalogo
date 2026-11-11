package com.turnos.catalogo.catalog.application.usecases;

import com.turnos.catalogo.catalog.application.exception.CatalogVersionGapException;
import com.turnos.catalogo.catalog.domain.model.CatalogChange;
import com.turnos.catalogo.catalog.domain.ports.in.ApplyCatalogChangeUseCase;
import com.turnos.catalogo.catalog.domain.ports.out.CatalogVersionRepository;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalCategoryRepository;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalRepository;
import com.turnos.catalogo.catalog.domain.ports.out.WeeklyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ApplyCatalogChangeUseCaseImpl implements ApplyCatalogChangeUseCase {

    private final ProfessionalCategoryRepository categoryRepository;
    private final ProfessionalRepository professionalRepository;
    private final WeeklyScheduleRepository scheduleRepository;
    private final CatalogVersionRepository versionRepository;

    // Una versión por transacción: la versión local avanza solo si sus cambios
    // quedaron guardados (contrato 14.5). Repetir una versión ya aplicada no hace
    // nada y saltear una no se permite.
    @Override
    @Transactional
    public boolean applyChange(CatalogChange change) {
        Long localVersion = versionRepository.findCurrentVersion().orElse(null);
        if (localVersion != null && change.version() <= localVersion) {
            return false;
        }
        if (localVersion == null || change.version() != localVersion + 1) {
            throw new CatalogVersionGapException(localVersion, change.version());
        }
        // Orden de las claves foráneas: categorías, profesionales, agendas.
        change.categories().forEach(categoryRepository::save);
        change.professionals().forEach(professionalRepository::save);
        change.schedules().forEach(scheduleRepository::save);
        versionRepository.saveVersion(change.version());
        return true;
    }
}
