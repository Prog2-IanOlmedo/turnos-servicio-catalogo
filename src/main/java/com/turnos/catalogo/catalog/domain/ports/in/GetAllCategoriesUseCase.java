package com.turnos.catalogo.catalog.domain.ports.in;

import com.turnos.catalogo.catalog.domain.model.ProfessionalCategory;

import java.util.List;

public interface GetAllCategoriesUseCase {
    List<ProfessionalCategory> getAllCategories();
}
