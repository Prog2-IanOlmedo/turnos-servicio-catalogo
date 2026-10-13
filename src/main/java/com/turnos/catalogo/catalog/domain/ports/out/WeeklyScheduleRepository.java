package com.turnos.catalogo.catalog.domain.ports.out;

import com.turnos.catalogo.catalog.domain.model.WeeklySchedule;

import java.util.List;
import java.util.Optional;

public interface WeeklyScheduleRepository {

    WeeklySchedule save(WeeklySchedule schedule);
    Optional<WeeklySchedule> findById(Long id);
    List<WeeklySchedule> findAll();

}
