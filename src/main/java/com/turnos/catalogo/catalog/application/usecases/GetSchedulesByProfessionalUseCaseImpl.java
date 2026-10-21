package com.turnos.catalogo.catalog.application.usecases;

import com.turnos.catalogo.catalog.domain.model.WeeklySchedule;
import com.turnos.catalogo.catalog.domain.ports.in.GetSchedulesByProfessionalUseCase;
import com.turnos.catalogo.catalog.domain.ports.out.WeeklyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetSchedulesByProfessionalUseCaseImpl implements GetSchedulesByProfessionalUseCase {

    private final WeeklyScheduleRepository repository;

    @Override
    public List<WeeklySchedule> getSchedulesByProfessional(Long professionalId) {
        return repository.findByProfessionalId(professionalId).stream()
                .filter(WeeklySchedule::isEnabled)
                .toList();
    }
}
