package com.turnos.catalogo.catalog.infrastructure.persistence.adapter;

import com.turnos.catalogo.catalog.domain.model.WeeklySchedule;
import com.turnos.catalogo.catalog.domain.ports.out.WeeklyScheduleRepository;
import com.turnos.catalogo.catalog.infrastructure.persistence.mapper.WeeklyScheduleMapper;
import com.turnos.catalogo.catalog.infrastructure.persistence.repository.JpaWeeklyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaWeeklyScheduleRepositoryAdapter implements WeeklyScheduleRepository {

    private final JpaWeeklyScheduleRepository jpaRepository;
    private final WeeklyScheduleMapper mapper;

    @Override
    public WeeklySchedule save(WeeklySchedule schedule) {
        return mapper.toDomainModel(jpaRepository.save(mapper.toEntity(schedule)));
    }

    @Override
    public Optional<WeeklySchedule> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomainModel);
    }

    @Override
    public List<WeeklySchedule> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomainModel)
                .toList();
    }
}
