package com.turnos.catalogo.catalog.infrastructure.client;

import com.turnos.catalogo.catalog.application.exception.SnapshotUnavailableException;
import com.turnos.catalogo.catalog.domain.model.CatalogSnapshot;
import com.turnos.catalogo.catalog.domain.ports.out.CatalogSnapshotProvider;
import com.turnos.catalogo.catalog.infrastructure.client.dto.CatedraSnapshotDto;
import com.turnos.catalogo.catalog.infrastructure.client.mapper.CatedraSnapshotMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

// Adaptador del puerto CatalogSnapshotProvider contra GET /api/synchronization/snapshot.
// Cualquier falla (401, 5xx, timeout, JSON roto) sale como SnapshotUnavailableException.
@Component
@RequiredArgsConstructor
public class CatedraSnapshotClient implements CatalogSnapshotProvider {

    private static final String SNAPSHOT_PATH = "/api/synchronization/snapshot";

    private final RestClient catedraRestClient;
    private final CatedraSnapshotMapper mapper;

    @Override
    public CatalogSnapshot fetchSnapshot() {
        CatedraSnapshotDto dto;
        try {
            dto = catedraRestClient.get()
                    .uri(SNAPSHOT_PATH)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(CatedraSnapshotDto.class);
        } catch (RestClientException e) {
            throw new SnapshotUnavailableException("No se pudo obtener el snapshot de la cátedra", e);
        }
        if (dto == null) {
            throw new SnapshotUnavailableException("La cátedra respondió el snapshot sin cuerpo");
        }
        return mapper.toDomainModel(dto);
    }
}
