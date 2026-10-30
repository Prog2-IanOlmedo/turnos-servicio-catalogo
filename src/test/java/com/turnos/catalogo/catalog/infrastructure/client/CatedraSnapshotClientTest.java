package com.turnos.catalogo.catalog.infrastructure.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withUnauthorizedRequest;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.turnos.catalogo.catalog.application.exception.SnapshotUnavailableException;
import com.turnos.catalogo.catalog.domain.model.CatalogSnapshot;
import com.turnos.catalogo.catalog.domain.model.WeeklySchedule;
import com.turnos.catalogo.catalog.infrastructure.client.mapper.CatedraSnapshotMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class CatedraSnapshotClientTest {

    private static final String SNAPSHOT_URL = "http://catedra.test/api/synchronization/snapshot";

    // Mismo formato que la sección 7 del contrato. Le agrego una hora HH:mm
    // y un campo desconocido para ver que no rompen la lectura.
    private static final String SNAPSHOT_JSON = """
            {
              "snapshotVersion": 7,
              "generatedAt": "2026-09-10T13:44:59Z",
              "campoNuevo": "lo tiene que ignorar",
              "professionalCategories": [
                {"id": 1, "name": "Clinica medica", "description": "Atencion clinica general",
                 "enabled": true, "createdAt": "2026-07-01T12:00:00Z", "updatedAt": "2026-09-10T13:40:00Z"}
              ],
              "professionals": [
                {"id": 15, "categoryId": 1, "firstName": "Ana", "lastName": "Gomez",
                 "enabled": true, "createdAt": "2026-07-01T12:00:00Z", "updatedAt": "2026-09-10T13:40:00Z"}
              ],
              "weeklySchedules": [
                {"id": 21, "professionalId": 15, "dayOfWeek": "MONDAY", "startTime": "09:00",
                 "endTime": "12:00:00", "slotDurationMinutes": 30, "enabled": true,
                 "createdAt": "2026-07-01T12:00:00Z", "updatedAt": "2026-09-10T13:40:00Z"}
              ]
            }
            """;

    private MockRestServiceServer server;
    private CatedraSnapshotClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder()
                .baseUrl("http://catedra.test")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer jwt-de-prueba");
        server = MockRestServiceServer.bindTo(builder).build();
        client = new CatedraSnapshotClient(builder.build(), new CatedraSnapshotMapper());
    }

    @Test
    void traeElSnapshotYLoPasaAlDominio() {
        server.expect(requestTo(SNAPSHOT_URL))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer jwt-de-prueba"))
                .andRespond(withSuccess(SNAPSHOT_JSON, MediaType.APPLICATION_JSON));

        CatalogSnapshot snapshot = client.fetchSnapshot();

        server.verify();
        assertThat(snapshot.getSnapshotVersion()).isEqualTo(7L);
        assertThat(snapshot.getCategories()).hasSize(1);
        assertThat(snapshot.getProfessionals()).hasSize(1);
        assertThat(snapshot.getSchedules()).hasSize(1);
        WeeklySchedule agenda = snapshot.getSchedules().get(0);
        assertThat(agenda.getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(agenda.getStartTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(agenda.getEndTime()).isEqualTo(LocalTime.of(12, 0));
        assertThat(snapshot.hasConsistentReferences()).isTrue();
    }

    @Test
    void fallaSiLaCatedraRechazaElJwt() {
        server.expect(requestTo(SNAPSHOT_URL)).andRespond(withUnauthorizedRequest());

        assertThatThrownBy(() -> client.fetchSnapshot())
                .isInstanceOf(SnapshotUnavailableException.class);
    }

    @Test
    void fallaSiLaCatedraTieneUnError() {
        server.expect(requestTo(SNAPSHOT_URL)).andRespond(withServerError());

        assertThatThrownBy(() -> client.fetchSnapshot())
                .isInstanceOf(SnapshotUnavailableException.class);
    }

    @Test
    void fallaSiLaRespuestaVieneVacia() {
        server.expect(requestTo(SNAPSHOT_URL)).andRespond(withSuccess());

        assertThatThrownBy(() -> client.fetchSnapshot())
                .isInstanceOf(SnapshotUnavailableException.class)
                .hasMessageContaining("sin cuerpo");
    }
}
