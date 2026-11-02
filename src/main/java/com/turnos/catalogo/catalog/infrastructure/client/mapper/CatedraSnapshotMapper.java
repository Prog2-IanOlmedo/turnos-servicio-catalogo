package com.turnos.catalogo.catalog.infrastructure.client.mapper;

import java.util.List;
import java.util.function.Function;

import com.turnos.catalogo.catalog.domain.model.CatalogSnapshot;
import com.turnos.catalogo.catalog.domain.model.Professional;
import com.turnos.catalogo.catalog.domain.model.ProfessionalCategory;
import com.turnos.catalogo.catalog.domain.model.WeeklySchedule;
import com.turnos.catalogo.catalog.infrastructure.client.dto.CatedraCategoryDto;
import com.turnos.catalogo.catalog.infrastructure.client.dto.CatedraProfessionalDto;
import com.turnos.catalogo.catalog.infrastructure.client.dto.CatedraScheduleDto;
import com.turnos.catalogo.catalog.infrastructure.client.dto.CatedraSnapshotDto;
import org.springframework.stereotype.Component;

// Pasa el JSON de la cátedra al modelo de dominio.
// Si falta una lista la tomo como vacía, así hasConsistentReferences no explota.
@Component
public class CatedraSnapshotMapper {

    public CatalogSnapshot toDomainModel(CatedraSnapshotDto dto) {
        if (dto == null) {
            return null;
        }
        return CatalogSnapshot.builder()
                .snapshotVersion(dto.snapshotVersion())
                .generatedAt(dto.generatedAt())
                .categories(mapList(dto.professionalCategories(), this::toCategory))
                .professionals(mapList(dto.professionals(), this::toProfessional))
                .schedules(mapList(dto.weeklySchedules(), this::toSchedule))
                .build();
    }

    private ProfessionalCategory toCategory(CatedraCategoryDto dto) {
        return ProfessionalCategory.builder()
                .id(dto.id())
                .name(dto.name())
                .description(dto.description())
                .enabled(dto.enabled())
                .createdAt(dto.createdAt())
                .updatedAt(dto.updatedAt())
                .build();
    }

    private Professional toProfessional(CatedraProfessionalDto dto) {
        return Professional.builder()
                .id(dto.id())
                .categoryId(dto.categoryId())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .enabled(dto.enabled())
                .createdAt(dto.createdAt())
                .updatedAt(dto.updatedAt())
                .build();
    }

    private WeeklySchedule toSchedule(CatedraScheduleDto dto) {
        return WeeklySchedule.builder()
                .id(dto.id())
                .professionalId(dto.professionalId())
                .dayOfWeek(dto.dayOfWeek())
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .slotDurationMinutes(dto.slotDurationMinutes())
                .enabled(dto.enabled())
                .createdAt(dto.createdAt())
                .updatedAt(dto.updatedAt())
                .build();
    }

    private <T, R> List<R> mapList(List<T> items, Function<T, R> mapper) {
        return items == null ? List.of() : items.stream().map(mapper).toList();
    }
}
