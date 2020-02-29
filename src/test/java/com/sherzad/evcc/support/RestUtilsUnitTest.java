package com.sherzad.evcc.support;

import com.github.javafaker.Faker;
import com.sherzad.evcc.domain.Client;

import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RestUtilsUnitTest {

    private RestUtils restUtils = new RestUtils();

    @Test
    void shouldCreateResponse() {
        // given
        final Faker faker = new Faker();
        final Client client = Client.builder()
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .build();

        // when
        final ResponseEntity<Client> response = restUtils.response(client);

        // then
        assertThat(response.getHeaders()).isEmpty();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(client);
    }

    @Test
    void shouldThrowException() {
        // when... then...
        assertThrows(ResponseStatusException.class, () -> restUtils.response(null));
    }

    @Test
    void shouldCreateEmptyResponse() {
        // when
        final ResponseEntity<Void> emptyResponse = restUtils.emptyResponse();

        // then
        assertThat(emptyResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(emptyResponse.getHeaders()).isEmpty();
        assertThat(emptyResponse.getBody()).isNull();
    }
}
