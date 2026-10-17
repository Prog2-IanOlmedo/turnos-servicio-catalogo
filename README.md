<!-- calidad:inicio -->
![Calidad](https://img.shields.io/badge/Calidad-53%2F100-yellow) ![Aprobar](https://img.shields.io/badge/Aprobar-no-red) ![Destacar](https://img.shields.io/badge/Destacar-no-lightgrey) ![Build](https://img.shields.io/badge/Build-OK-green) ![Duplicación](https://img.shields.io/badge/Duplicaci%C3%B3n-29.2%25-orange) ![%POO](https://img.shields.io/badge/%25POO-63%25-yellow) ![Sueltas](https://img.shields.io/badge/Sueltas-0-green) ![D1](https://img.shields.io/badge/D1-12-red) ![D2](https://img.shields.io/badge/D2-0-red) ![D4](https://img.shields.io/badge/D4-0-red) ![D5](https://img.shields.io/badge/D5-100-green) ![D6](https://img.shields.io/badge/D6-100-green) ![D7](https://img.shields.io/badge/D7-100-green)

**Calidad (heurístico, 7 dimensiones):** índice **53/100** · build **OK** · aprobar **no (6/11)** · destacar **no**
Detalle completo en [`docs/EVALUACION.md`](docs/EVALUACION.md).
<!-- calidad:fin -->

# turnos-servicio-catalogo

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Prog2-IanOlmedo_turnos-servicio-catalogo&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Prog2-IanOlmedo_turnos-servicio-catalogo) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Prog2-IanOlmedo_turnos-servicio-catalogo&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Prog2-IanOlmedo_turnos-servicio-catalogo)

Servicio de catálogo y sincronización del proyecto integrador de Programación 2 (sistema distribuido de turnos). Caso de estudio académico con datos ficticios.

> Estado: esqueleto inicial. Todavía no se integra con la cátedra ni expone endpoints de negocio.

## Requisitos

- Java 25
- Docker y Docker Compose
- No hace falta instalar Maven: se usa el wrapper incluido (`./mvnw`).

## Configuración

1. Copiar la plantilla de variables de entorno: `cp .env.example .env`
2. Editar `.env` y reemplazar `CATALOGO_DB_PASSWORD` por una contraseña propia.

`.env` está en `.gitignore` y nunca debe subirse al repositorio.

## Ejecución

1. Levantar PostgreSQL: `docker compose up -d`
2. Iniciar el servicio: `./mvnw spring-boot:run`
3. Verificar: `curl http://localhost:8080/actuator/health` debe responder `{"status":"UP"}`

## Pruebas

`./mvnw test`

Requiere Docker: las pruebas levantan su propio PostgreSQL 17 con Testcontainers.
