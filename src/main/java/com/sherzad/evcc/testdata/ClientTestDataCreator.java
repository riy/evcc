package com.sherzad.evcc.testdata;

import javax.annotation.PostConstruct;

import com.github.javafaker.Faker;
import com.sherzad.evcc.common.Profiles;
import com.sherzad.evcc.domain.Client;
import com.sherzad.evcc.service.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static com.sherzad.evcc.domain.ClientStatusEnum.ACTIVE;
import static com.sherzad.evcc.domain.ClientStatusEnum.INACTIVE;

@Service
@Profile(Profiles.POPULATE)
public class ClientTestDataCreator {

    @Autowired
    private ClientService clientService;

    @PostConstruct
    public void init() {
        final Faker faker = new Faker();
        for (int i = 0; i < 250; i++) {
            clientService.save(
                    Client.builder()
                            .firstname(faker.name().firstName())
                            .lastname(faker.name().lastName())
                            .telephone(faker.phoneNumber().cellPhone())
                            .email(faker.internet().emailAddress())
                            .street(faker.address().streetAddress())
                            .zipCode(faker.address().zipCode())
                            .city(faker.address().city())
                            .country(faker.address().country())
                            .status(faker.random().nextBoolean() ? ACTIVE : INACTIVE)
                            .build());
        }
    }
}
