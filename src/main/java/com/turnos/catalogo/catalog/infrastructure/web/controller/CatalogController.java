package com.turnos.catalogo.catalog.infrastructure.web.controller;

import com.turnos.catalogo.catalog.application.service.CatalogService;
import com.turnos.catalogo.catalog.domain.model.ProfessionalFilter;
import com.turnos.catalogo.catalog.infrastructure.web.dto.CategoryResponse;
import com.turnos.catalogo.catalog.infrastructure.web.dto.ProfessionalResponse;
import com.turnos.catalogo.catalog.infrastructure.web.dto.ScheduleResponse;
import com.turnos.catalogo.catalog.infrastructure.web.mapper.CatalogDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;
    private final CatalogDtoMapper catalogDtoMapper;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(catalogService.getAllCategories().stream()
                .map(catalogDtoMapper::toCategoryResponse)
                .toList());
    }

    @GetMapping("/professionals")
    // Filtros opcionales: ?categoryId=101 y/o ?name=riv. Sin parámetros, todos los habilitados.
    public ResponseEntity<List<ProfessionalResponse>> getProfessionals(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(catalogService.searchProfessionals(new ProfessionalFilter(categoryId, name)).stream()
                .map(catalogDtoMapper::toProfessionalResponse)
                .toList());
    }

    @GetMapping("/professionals/{id}")
    public ResponseEntity<ProfessionalResponse> getProfessionalById(@PathVariable Long id) {
        return ResponseEntity.ok(catalogDtoMapper.toProfessionalResponse(catalogService.getProfessionalById(id)));
    }

    @GetMapping("/professionals/{id}/schedules")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByProfessional(@PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getSchedulesByProfessional(id).stream()
                .map(catalogDtoMapper::toScheduleResponse)
                .toList());
    }
}
