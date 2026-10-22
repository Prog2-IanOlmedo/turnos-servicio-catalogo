package com.turnos.catalogo.catalog.application.usecases;

import com.turnos.catalogo.catalog.domain.model.ProfessionalCategory;
import com.turnos.catalogo.catalog.domain.ports.in.GetAllCategoriesUseCase;
import com.turnos.catalogo.catalog.domain.ports.out.ProfessionalCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllCategoriesUseCaseImpl implements GetAllCategoriesUseCase {

    private final ProfessionalCategoryRepository repository;

    // Lo deshabilitado se guarda (viene en el snapshot) pero no se ofrece.
    @Override
    public List<ProfessionalCategory> getAllCategories() {
        return repository.findAll().stream()
                .filter(ProfessionalCategory::isEnabled)
                .toList();
    }
}
