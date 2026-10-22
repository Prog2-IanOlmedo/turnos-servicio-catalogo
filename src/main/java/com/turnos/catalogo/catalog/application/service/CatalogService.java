package com.turnos.catalogo.catalog.application.service;

import com.turnos.catalogo.catalog.application.exception.ProfessionalNotFoundException;
import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.domain.model.ProfessionalCategory;
import com.turnos.catalogo.catalog.domain.model.WeeklySchedule;
import com.turnos.catalogo.catalog.domain.ports.in.GetAllCategoriesUseCase;
import com.turnos.catalogo.catalog.domain.ports.in.GetAllProfessionalsUseCase;
import com.turnos.catalogo.catalog.domain.ports.in.GetProfessionalByIdUseCase;
import com.turnos.catalogo.catalog.domain.ports.in.GetSchedulesByProfessionalUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final GetAllCategoriesUseCase getAllCategoriesUseCase;
    private final GetAllProfessionalsUseCase getAllProfessionalsUseCase;
    private final GetProfessionalByIdUseCase getProfessionalByIdUseCase;
    private final GetSchedulesByProfessionalUseCase getSchedulesByProfessionalUseCase;

    public List<ProfessionalCategory> getAllCategories() {
        return getAllCategoriesUseCase.getAllCategories();
    }

    public List<Professional> getAllProfessionals() {
        return getAllProfessionalsUseCase.getAllProfessionals();
    }

    public Professional getProfessionalById(Long id) {
        return getProfessionalByIdUseCase.getProfessionalById(id)
                .orElseThrow(() -> new ProfessionalNotFoundException(id));
    }

    // Si el profesional no existe (o está deshabilitado) se informa,
    // en lugar de devolver una lista vacía que no distingue los casos.
    public List<WeeklySchedule> getSchedulesByProfessional(Long professionalId) {
        getProfessionalById(professionalId);
        return getSchedulesByProfessionalUseCase.getSchedulesByProfessional(professionalId);
    }
}
