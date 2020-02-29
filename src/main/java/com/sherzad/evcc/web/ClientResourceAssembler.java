package com.sherzad.evcc.web;

import com.sherzad.evcc.domain.Client;
import com.sherzad.evcc.domain.ClientStatusEnum;

import org.springframework.stereotype.Component;

@Component
public class ClientResourceAssembler {

    public ClientResource toResource(final Client client) {
        if (client != null) {
            return ClientResource.builder()
                    .id(client.getId())
                    .firstname(client.getFirstname())
                    .lastname(client.getLastname())
                    .telephone(client.getTelephone())
                    .email(client.getEmail())
                    .street(client.getStreet())
                    .zipCode(client.getZipCode())
                    .city(client.getCity())
                    .country(client.getCountry())
                    .status(client.getStatus().getIdentifier())
                    .build();
        } else {
            return null;
        }
    }

    public Client toModel(final ClientResource clientResource) {
        if (clientResource != null) {
            return Client.builder()
                    .id(clientResource.getId())
                    .firstname(clientResource.getFirstname())
                    .lastname(clientResource.getLastname())
                    .telephone(clientResource.getTelephone())
                    .email(clientResource.getEmail())
                    .street(clientResource.getStreet())
                    .zipCode(clientResource.getZipCode())
                    .city(clientResource.getCity())
                    .country(clientResource.getCountry())
                    .status(ClientStatusEnum.byIdentifier(clientResource.getStatus()))
                    .build();
        } else {
            return null;
        }
    }
}
