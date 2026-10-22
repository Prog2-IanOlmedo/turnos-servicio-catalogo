package com.turnos.catalogo.catalog.domain.ports.in;

import com.turnos.catalogo.catalog.domain.model.WeeklySchedule;

import java.util.List;

public interface GetSchedulesByProfessionalUseCase {
    List<WeeklySchedule> getSchedulesByProfessional(Long professionalId);
}
