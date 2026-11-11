package com.turnos.catalogo.catalog.infrastructure.security;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtConfigTest {

    private final JwtConfig config = new JwtConfig();

    @Test
    void sinClaveUsaUnaAlAzar() {
        SecretKey key = config.jwtSecretKey("");

        assertThat(key.getEncoded()).hasSize(JwtConfig.MIN_KEY_BYTES);
        assertThat(key.getAlgorithm()).isEqualTo(JwtConfig.ALGORITHM);
    }

    @Test
    void conUnaClaveCortaNoArranca() {
        String corta = Base64.getEncoder().encodeToString(new byte[32]);

        assertThatThrownBy(() -> config.jwtSecretKey(corta)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void usaLaClaveCargada() {
        byte[] bytes = new byte[64];
        bytes[0] = 7;

        SecretKey key = config.jwtSecretKey(Base64.getEncoder().encodeToString(bytes));

        assertThat(key.getEncoded()).isEqualTo(bytes);
    }
}
