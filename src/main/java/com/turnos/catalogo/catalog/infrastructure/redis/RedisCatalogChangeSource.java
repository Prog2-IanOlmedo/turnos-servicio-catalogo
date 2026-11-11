package com.turnos.catalogo.catalog.infrastructure.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.turnos.catalogo.catalog.application.exception.CatalogChangeUnavailableException;
import com.turnos.catalogo.catalog.domain.model.CatalogChange;
import com.turnos.catalogo.catalog.domain.model.CatalogSnapshot;
import com.turnos.catalogo.catalog.domain.model.CatalogVersionWindow;
import com.turnos.catalogo.catalog.domain.ports.out.CatalogChangeSource;
import com.turnos.catalogo.catalog.infrastructure.client.dto.CatedraCategoryDto;
import com.turnos.catalogo.catalog.infrastructure.client.dto.CatedraProfessionalDto;
import com.turnos.catalogo.catalog.infrastructure.client.dto.CatedraScheduleDto;
import com.turnos.catalogo.catalog.infrastructure.client.dto.CatedraSnapshotDto;
import com.turnos.catalogo.catalog.infrastructure.client.mapper.CatedraSnapshotMapper;
import com.turnos.catalogo.catalog.infrastructure.redis.dto.CatedraChangeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

// Adaptador del puerto CatalogChangeSource contra el Redis de la cátedra (contrato 14).
// changes:{version} trae solo ids: el estado vigente se lee de cada hash con HMGET.
// Las entidades tienen el mismo JSON que el snapshot, así que reutilizo su mapper.
@Component
@RequiredArgsConstructor
public class RedisCatalogChangeSource implements CatalogChangeSource {

    private static final String METADATA = "catedra:sync:metadata";
    private static final String CHANGES = "catedra:sync:changes:";
    private static final String CATEGORIES = "catedra:sync:professional-categories";
    private static final String PROFESSIONALS = "catedra:sync:professionals";
    private static final String SCHEDULES = "catedra:sync:weekly-schedules";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final CatedraSnapshotMapper snapshotMapper;

    @Override
    public CatalogVersionWindow currentWindow() {
        List<Object> values = redisTemplate.opsForHash()
                .multiGet(METADATA, List.of("currentVersion", "oldestAvailableVersion"));
        return new CatalogVersionWindow(parseVersion(values.get(0)), parseVersion(values.get(1)));
    }

    @Override
    public Optional<CatalogChange> findChange(long version) {
        String json = redisTemplate.opsForValue().get(CHANGES + version);
        if (json == null) {
            return Optional.empty();
        }
        CatedraChangeDto.Changes ids = objectMapper.readValue(json, CatedraChangeDto.class).changes();
        CatalogSnapshot entities = snapshotMapper.toDomainModel(new CatedraSnapshotDto(version, null,
                read(CATEGORIES, ids.professionalCategories(), CatedraCategoryDto.class),
                read(PROFESSIONALS, ids.professionals(), CatedraProfessionalDto.class),
                read(SCHEDULES, ids.weeklySchedules(), CatedraScheduleDto.class)));
        return Optional.of(new CatalogChange(version, entities.getCategories(),
                entities.getProfessionals(), entities.getSchedules()));
    }

    private <T> List<T> read(String hash, List<Long> ids, Class<T> type) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        List<Object> fields = ids.stream().map(id -> (Object) id.toString()).toList();
        List<Object> values = redisTemplate.opsForHash().multiGet(hash, fields);
        List<T> result = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            Object value = values.get(i);
            if (value == null) {
                throw new CatalogChangeUnavailableException("No está en Redis el id " + ids.get(i) + " de " + hash);
            }
            result.add(objectMapper.readValue(value.toString(), type));
        }
        return result;
    }

    private long parseVersion(Object value) {
        if (value == null) {
            throw new CatalogChangeUnavailableException("Falta la metadata de versiones en " + METADATA);
        }
        return Long.parseLong(value.toString());
    }
}
