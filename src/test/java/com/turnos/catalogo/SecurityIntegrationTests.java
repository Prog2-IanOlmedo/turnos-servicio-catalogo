package com.turnos.catalogo;

import java.security.SecureRandom;
import java.time.Instant;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class SecurityIntegrationTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private SecretKey jwtSecretKey;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    // Armo el token como lo emite reservas: HS512, sub y el claim "auth".
    private String token(SecretKey key, Instant expiresAt) {
        JwtClaimsSet claims = JwtClaimsSet.builder().subject("ian-prueba").claim("auth", "ROLE_USER")
                .issuedAt(expiresAt.minusSeconds(3600)).expiresAt(expiresAt).build();
        return new NimbusJwtEncoder(new ImmutableSecret<>(key))
                .encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS512).build(), claims))
                .getTokenValue();
    }

    private SecretKey otraClave() {
        byte[] bytes = new byte[64];
        new SecureRandom().nextBytes(bytes);
        return new SecretKeySpec(bytes, "HmacSHA512");
    }

    @Test
    void sinTokenResponde401() throws Exception {
        mockMvc.perform(get("/api/categories")).andExpect(status().isUnauthorized());
    }

    @Test
    void conTokenValidoResponde() throws Exception {
        String token = token(jwtSecretKey, Instant.now().plusSeconds(3600));

        mockMvc.perform(get("/api/categories").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void tokenFirmadoConOtraClaveResponde401() throws Exception {
        String token = token(otraClave(), Instant.now().plusSeconds(3600));

        mockMvc.perform(get("/api/professionals").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void tokenVencidoResponde401() throws Exception {
        String token = token(jwtSecretKey, Instant.now().minusSeconds(3600));

        mockMvc.perform(get("/api/professionals").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void elHealthNoPideToken() throws Exception {
        int status = mockMvc.perform(get("/actuator/health")).andReturn().getResponse().getStatus();

        assertThat(status).isNotIn(401, 403);
    }
}
