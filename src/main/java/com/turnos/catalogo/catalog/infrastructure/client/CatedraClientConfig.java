package com.turnos.catalogo.catalog.infrastructure.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

// Cliente HTTP hacia la cátedra. Todos los valores salen de application.properties,
// que a su vez los toma del entorno. El JWT técnico va en cada request (contrato, sección 4).
@Configuration
public class CatedraClientConfig {

    @Bean
    RestClient catedraRestClient(
            @Value("${catedra.api.url}") String url,
            @Value("${catedra.api.jwt}") String jwt,
            @Value("${catedra.api.connect-timeout}") Duration connectTimeout,
            @Value("${catedra.api.read-timeout}") Duration readTimeout) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);
        return RestClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .requestFactory(requestFactory)
                .build();
    }
}
