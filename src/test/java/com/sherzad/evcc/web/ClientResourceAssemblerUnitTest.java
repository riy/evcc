package com.sherzad.evcc.web;

import com.sherzad.evcc.domain.Client;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import static com.sherzad.evcc.util.ClientUtil.createFakeClient;
import static com.sherzad.evcc.util.ClientUtil.createFakeClientResource;

class ClientResourceAssemblerUnitTest {

    private ClientResourceAssembler clientResourceAssembler = new ClientResourceAssembler();

    @Test
    void shouldConvertToResource() {
        // given
        final Client client = createFakeClient();

        // when
        final ClientResource clientResource = clientResourceAssembler.toResource(client);

        // then
        assertThat(clientResource.getId()).isEqualTo(client.getId());
        assertThat(clientResource.getFirstname()).isEqualTo(client.getFirstname());
        assertThat(clientResource.getLastname()).isEqualTo(client.getLastname());
        assertThat(clientResource.getTelephone()).isEqualTo(client.getTelephone());
        assertThat(clientResource.getEmail()).isEqualTo(client.getEmail());
        assertThat(clientResource.getStreet()).isEqualTo(client.getStreet());
        assertThat(clientResource.getZipCode()).isEqualTo(client.getZipCode());
        assertThat(clientResource.getCity()).isEqualTo(client.getCity());
        assertThat(clientResource.getCountry()).isEqualTo(client.getCountry());
        assertThat(clientResource.getStatus()).isEqualTo(client.getStatus().getIdentifier());
    }

    @Test
    void shouldNotConvertToResource() {
        // when
        final ClientResource nullClientResource = clientResourceAssembler.toResource(null);

        // then
        assertThat(nullClientResource).isNull();
    }

    @Test
    void shouldConvertToModel() {
        // given
        final ClientResource clientResource = createFakeClientResource();

        // when
        final Client client = clientResourceAssembler.toModel(clientResource);

        // then
        assertThat(client.getId()).isEqualTo(clientResource.getId());
        assertThat(client.getFirstname()).isEqualTo(clientResource.getFirstname());
        assertThat(client.getLastname()).isEqualTo(clientResource.getLastname());
        assertThat(client.getTelephone()).isEqualTo(clientResource.getTelephone());
        assertThat(client.getEmail()).isEqualTo(clientResource.getEmail());
        assertThat(client.getStreet()).isEqualTo(clientResource.getStreet());
        assertThat(client.getZipCode()).isEqualTo(clientResource.getZipCode());
        assertThat(client.getCity()).isEqualTo(clientResource.getCity());
        assertThat(client.getCountry()).isEqualTo(clientResource.getCountry());
        assertThat(client.getStatus().getIdentifier()).isEqualTo(clientResource.getStatus());
    }

    @Test
    void shouldNotConvertToModel() {
        // when
        final Client nullClient = clientResourceAssembler.toModel(null);

        // then
        assertThat(nullClient).isNull();
    }
}
