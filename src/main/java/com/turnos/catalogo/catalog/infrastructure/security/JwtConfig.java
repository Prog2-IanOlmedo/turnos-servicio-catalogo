package com.turnos.catalogo.catalog.infrastructure.security;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

// El catálogo solo valida los JWT de los usuarios; los emite reservas. Uso la misma
// clave (JWT_SECRET, Base64, mínimo 64 bytes) y el mismo HS512.
@Slf4j
@Configuration
public class JwtConfig {

    static final String ALGORITHM = "HmacSHA512";
    static final int MIN_KEY_BYTES = 64;
    private static final SecureRandom RANDOM = new SecureRandom();

    // Sin clave genero una al azar para que levanten los tests y el CI sin secretos
    // en el repo. Con una clave cargada pero corta, no arranco.
    @Bean
    SecretKey jwtSecretKey(@Value("${security.jwt.secret:}") String secret) {
        if (secret == null || secret.isBlank()) {
            log.warn("JWT_SECRET vacía: uso una clave al azar y no voy a aceptar los tokens de reservas.");
            byte[] random = new byte[MIN_KEY_BYTES];
            RANDOM.nextBytes(random);
            return new SecretKeySpec(random, ALGORITHM);
        }
        byte[] bytes = Base64.getDecoder().decode(secret.trim());
        if (bytes.length < MIN_KEY_BYTES) {
            throw new IllegalStateException("JWT_SECRET tiene que tener al menos " + MIN_KEY_BYTES + " bytes");
        }
        return new SecretKeySpec(bytes, ALGORITHM);
    }

    @Bean
    JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {
        return NimbusJwtDecoder.withSecretKey(jwtSecretKey).macAlgorithm(MacAlgorithm.HS512).build();
    }
}
