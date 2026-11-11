package com.turnos.catalogo.catalog.infrastructure.redis;

import java.util.Map;

import com.turnos.catalogo.catalog.application.exception.CatalogChangeUnavailableException;
import com.turnos.catalogo.catalog.domain.model.CatalogChange;
import com.turnos.catalogo.catalog.domain.model.CatalogVersionWindow;
import com.turnos.catalogo.catalog.infrastructure.client.mapper.CatedraSnapshotMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Contra un Redis real, con datos copiados del Redis de la cátedra.
@Testcontainers
class RedisCatalogChangeSourceTest {

    @Container
    static final GenericContainer<?> REDIS = new GenericContainer<>(DockerImageName.parse("redis:7"))
            .withExposedPorts(6379);

    private LettuceConnectionFactory connectionFactory;
    private StringRedisTemplate redisTemplate;
    private RedisCatalogChangeSource source;

    @BeforeEach
    void setUp() {
        connectionFactory = new LettuceConnectionFactory(REDIS.getHost(), REDIS.getMappedPort(6379));
        connectionFactory.afterPropertiesSet();
        connectionFactory.start();
        redisTemplate = new StringRedisTemplate(connectionFactory);
        redisTemplate.delete(redisTemplate.keys("catedra:*"));
        source = new RedisCatalogChangeSource(redisTemplate, JsonMapper.builder().build(), new CatedraSnapshotMapper());
    }

    @AfterEach
    void tearDown() {
        connectionFactory.destroy();
    }

    @Test
    void leeLaVentanaDeVersionesDeLaMetadata() {
        redisTemplate.opsForHash().putAll("catedra:sync:metadata", Map.of(
                "currentVersion", "148", "oldestAvailableVersion", "49",
                "publishedAt", "2026-11-10T12:15:51.269857221Z"));

        assertThat(source.currentWindow()).isEqualTo(new CatalogVersionWindow(148, 49));
    }

    @Test
    void armaElCambioConElEstadoVigenteDeCadaId() {
        redisTemplate.opsForValue().set("catedra:sync:changes:148",
                "{\"version\":148,\"changes\":{\"professionalCategories\":[],\"professionals\":[201],\"weeklySchedules\":[]}}");
        redisTemplate.opsForHash().put("catedra:sync:professionals", "201",
                "{\"id\":201,\"categoryId\":101,\"firstName\":\"Ana\",\"lastName\":\"Torres\",\"enabled\":false,"
                        + "\"createdAt\":\"2026-09-01T03:00:00Z\",\"updatedAt\":\"2026-11-10T12:15:51.248170Z\"}");

        CatalogChange cambio = source.findChange(148).orElseThrow();

        assertThat(cambio.version()).isEqualTo(148);
        assertThat(cambio.categories()).isEmpty();
        assertThat(cambio.schedules()).isEmpty();
        assertThat(cambio.professionals()).hasSize(1);
        assertThat(cambio.professionals().get(0).getId()).isEqualTo(201L);
        assertThat(cambio.professionals().get(0).isEnabled()).isFalse();
    }

    @Test
    void unaVersionQueYaNoEstaDevuelveVacio() {
        assertThat(source.findChange(12)).isEmpty();
    }

    @Test
    void siFaltaUnaEntidadDelCambioAvisa() {
        redisTemplate.opsForValue().set("catedra:sync:changes:149",
                "{\"version\":149,\"changes\":{\"professionalCategories\":[],\"professionals\":[999],\"weeklySchedules\":[]}}");

        assertThatThrownBy(() -> source.findChange(149))
                .isInstanceOf(CatalogChangeUnavailableException.class)
                .hasMessageContaining("999");
    }

    @Test
    void sinMetadataAvisa() {
        assertThatThrownBy(() -> source.currentWindow()).isInstanceOf(CatalogChangeUnavailableException.class);
    }
}
