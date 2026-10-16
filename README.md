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
