package com.turnos.catalogo.catalog.infrastructure.web.mapper;

import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.domain.model.ProfessionalCategory;
import com.turnos.catalogo.catalog.domain.model.WeeklySchedule;
import com.turnos.catalogo.catalog.infrastructure.web.dto.CategoryResponse;
import com.turnos.catalogo.catalog.infrastructure.web.dto.ProfessionalResponse;
import com.turnos.catalogo.catalog.infrastructure.web.dto.ScheduleResponse;
import org.springframework.stereotype.Component;

// enabled y las fechas de auditoría son internas: no se exponen.
@Component
public class CatalogDtoMapper {

    public CategoryResponse toCategoryResponse(ProfessionalCategory category) {
        if (category == null) {
            return null;
        }
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public ProfessionalResponse toProfessionalResponse(Professional professional) {
        if (professional == null) {
            return null;
        }
        return ProfessionalResponse.builder()
                .id(professional.getId())
                .categoryId(professional.getCategoryId())
                .firstName(professional.getFirstName())
                .lastName(professional.getLastName())
                .build();
    }

    public ScheduleResponse toScheduleResponse(WeeklySchedule schedule) {
        if (schedule == null) {
            return null;
        }
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .professionalId(schedule.getProfessionalId())
                .dayOfWeek(schedule.getDayOfWeek())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .slotDurationMinutes(schedule.getSlotDurationMinutes())
                .slots(schedule.slotStartTimes())
                .build();
    }
}
