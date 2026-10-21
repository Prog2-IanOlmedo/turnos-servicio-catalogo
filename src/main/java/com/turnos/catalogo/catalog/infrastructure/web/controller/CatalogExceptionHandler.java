package com.turnos.catalogo.catalog.infrastructure.web.controller;

import com.turnos.catalogo.catalog.application.exception.ProfessionalNotFoundException;
import com.turnos.catalogo.shared.web.ApiProblem;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Va antes que el handler general, que también atrapa Exception.
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = CatalogController.class)
public class CatalogExceptionHandler {

    @ExceptionHandler(ProfessionalNotFoundException.class)
    public ProblemDetail handleProfessionalNotFound(ProfessionalNotFoundException ex, HttpServletRequest request) {
        return ApiProblem.of(HttpStatus.NOT_FOUND, "PROFESSIONAL_NOT_FOUND", ex.getMessage(), request.getRequestURI());
    }
}
