-- Catálogo local: copia de lo que devuelve el snapshot de la cátedra.
-- Los id vienen de la cátedra, por eso no son autoincrementales.
-- No se borra nada: lo deshabilitado queda con enabled = false.
-- Instantes en UTC -> TIMESTAMPTZ. Horas de atención (locales) -> TIME.

CREATE TABLE professional_category (
    id          BIGINT      PRIMARY KEY,
    name        TEXT        NOT NULL,
    description TEXT,
    enabled     BOOLEAN     NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL,
    updated_at  TIMESTAMPTZ NOT NULL
);

-- Un profesional siempre pertenece a una categoría (FK).
CREATE TABLE professional (
    id          BIGINT      PRIMARY KEY,
    category_id BIGINT      NOT NULL REFERENCES professional_category (id),
    first_name  TEXT        NOT NULL,
    last_name   TEXT        NOT NULL,
    enabled     BOOLEAN     NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL,
    updated_at  TIMESTAMPTZ NOT NULL
);

-- Cada fila es un bloque de atención de un día de la semana;
-- los slots se calculan con start_time, end_time y slot_duration_minutes.
CREATE TABLE weekly_schedule (
    id                    BIGINT      PRIMARY KEY,
    professional_id       BIGINT      NOT NULL REFERENCES professional (id),
    day_of_week           VARCHAR(9)  NOT NULL
        CHECK (day_of_week IN ('MONDAY','TUESDAY','WEDNESDAY','THURSDAY',
                               'FRIDAY','SATURDAY','SUNDAY')),
    start_time            TIME        NOT NULL,
    end_time              TIME        NOT NULL,
    slot_duration_minutes INTEGER     NOT NULL CHECK (slot_duration_minutes > 0),
    enabled               BOOLEAN     NOT NULL,
    created_at            TIMESTAMPTZ NOT NULL,
    updated_at            TIMESTAMPTZ NOT NULL,
    CHECK (end_time > start_time)
);

-- Versión del catálogo que tengo aplicada. Una sola fila (id = 1).
-- Sin fila = todavía no se sincronizó nunca.
CREATE TABLE catalog_sync_state (
    id               SMALLINT    PRIMARY KEY DEFAULT 1 CHECK (id = 1),
    catalog_version  BIGINT      NOT NULL,
    updated_at       TIMESTAMPTZ NOT NULL
);

-- Postgres no crea índices solo sobre las FK; estos aceleran
-- las consultas por categoría y por profesional.
CREATE INDEX idx_professional_category ON professional (category_id);
CREATE INDEX idx_weekly_schedule_professional ON weekly_schedule (professional_id);
