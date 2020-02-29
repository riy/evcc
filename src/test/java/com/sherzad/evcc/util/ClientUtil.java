package com.sherzad.evcc.util;

import com.github.javafaker.Faker;
import com.sherzad.evcc.domain.Client;
import com.sherzad.evcc.web.ClientResource;

import static com.sherzad.evcc.domain.ClientStatusEnum.ACTIVE;
import static com.sherzad.evcc.domain.ClientStatusEnum.INACTIVE;

public class ClientUtil {

    public static final Faker FAKER = new Faker();

    public static Client createFakeClient() {
        return createFakeClientBuilder().build();
    }

    public static ClientResource createFakeClientResource() {
        return createFakeClientResourceBuilder().build();
    }

    public static Client.ClientBuilder createFakeClientBuilder() {
        return Client.builder()
                .firstname(FAKER.name().firstName())
                .lastname(FAKER.name().lastName())
                .telephone(FAKER.phoneNumber().cellPhone())
                .email(FAKER.internet().emailAddress())
                .street(FAKER.address().streetAddress())
                .zipCode(FAKER.address().zipCode())
                .city(FAKER.address().city())
                .country(FAKER.address().country())
                .status(FAKER.random().nextBoolean() ? ACTIVE : INACTIVE);
    }

    public static ClientResource.ClientResourceBuilder createFakeClientResourceBuilder() {
        return ClientResource.builder()
                .firstname(FAKER.name().firstName())
                .lastname(FAKER.name().lastName())
                .telephone(FAKER.phoneNumber().cellPhone())
                .email(FAKER.internet().emailAddress())
                .street(FAKER.address().streetAddress())
                .zipCode(FAKER.address().zipCode())
                .city(FAKER.address().city())
                .country(FAKER.address().country())
                .status(FAKER.random().nextBoolean() ? ACTIVE.getIdentifier() : INACTIVE.getIdentifier());
    }
}
