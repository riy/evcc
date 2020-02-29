package com.sherzad.evcc.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.sherzad.evcc.domain.Client;
import com.sherzad.evcc.domain.ClientStatusEnum;
import com.sherzad.evcc.web.ClientSearch;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.TransactionSystemException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static com.sherzad.evcc.util.ClientUtil.createFakeClient;
import static com.sherzad.evcc.util.ClientUtil.createFakeClientBuilder;

@SpringBootTest
class ClientServiceIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Test
    void shouldFindAllClients() {
        // when
        final Page<Client> clients = clientService.findAll(Pageable.unpaged());

        // then
        assertThat(clients).hasSizeGreaterThan(249);
    }

    @Test
    void shouldFindAllActiveClientsPaged() {
        // when
        final Page<Client> clients = clientService.findAllByStatusActive(PageRequest.of(0, 10));

        // then
        assertThat(clients.getContent()).hasSize(10);
    }

    @Test
    void shouldFindSpecificClients() {
        // when
        final Client fakeClient = createFakeClientBuilder().status(ClientStatusEnum.ACTIVE).build();
        clientService.save(fakeClient);
        final Page<Client> clients = clientService.findByClientSearch(
                ClientSearch.builder()
                        .lastname(fakeClient.getLastname())
                        .zipCode(fakeClient.getZipCode())
                        .city(fakeClient.getCity())
                        .country(fakeClient.getCountry())
                        .build(),
                PageRequest.of(0, 10));

        // then
        assertThat(clients.getContent()).hasSize(1);
    }

    @Test
    void shouldSaveAndFindClient() {
        // given
        final Client fakeClient = createFakeClient();
        final Client savedClient = clientService.save(fakeClient);

        // when
        final Client foundClient = clientService.findById(savedClient.getId()).orElse(null);

        // then
        assertThat(foundClient).isNotNull();
        assertThat(foundClient).usingRecursiveComparison().isEqualTo(fakeClient);
    }

    @Test
    void shouldSaveFindAndDeleteClient() {
        // given
        final Client fakeClient = createFakeClient();
        final Client savedClient = clientService.save(fakeClient);

        // when
        final Client foundClient = clientService.findById(savedClient.getId()).orElse(null);

        // then
        assertThat(foundClient).isNotNull();
        assertThat(foundClient).usingRecursiveComparison().isEqualTo(fakeClient);

        // when
        clientService.deleteById(foundClient.getId());
        final Client nonFoundClient = clientService.findById(foundClient.getId()).orElse(null);

        // then
        assertThat(nonFoundClient).isNull();
    }

    @Test
    void shouldSaveUpdateAndFindClient() {
        // given
        final Client fakeClient = createFakeClient();
        final Client savedClient = clientService.save(fakeClient);

        // when
        final Client foundClient = clientService.findById(savedClient.getId()).orElse(null);

        // then
        assertThat(foundClient).isNotNull();
        assertThat(foundClient).usingRecursiveComparison().isEqualTo(fakeClient);

        // given
        final Client renamedClient = foundClient.toBuilder().lastname("Juana").build();
        clientService.save(renamedClient);

        // when
        final Client foundRenamedClient = clientService.findById(savedClient.getId()).orElse(null);

        // then
        assertThat(foundRenamedClient).usingRecursiveComparison().isEqualTo(renamedClient);

    }

    @Test
    void shouldFailWhenSavingInvalidClient() {
        // given
        final Client invalidClient = Client.builder().build();

        // when... then...
        final TransactionSystemException transactionSystemException = assertThrows(
                TransactionSystemException.class,
                () -> clientService.save(invalidClient));

        final List<String> constraintViolationMessages = ((ConstraintViolationException) transactionSystemException
                .getMostSpecificCause()).getConstraintViolations()
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList());
        assertThat(constraintViolationMessages).containsExactlyInAnyOrder(
                "Please enter a valid firstname.",
                "Please enter a valid lastname.",
                "Please enter an email address.",
                "Please specify a status.");
    }

    @Test
    void shouldFailWhenSavingClientWithInvalidEmailAddress() {
        // given
        final Client clientWithInvalidEmail = createFakeClientBuilder().email("invalid-email-address").build();

        // when... then...
        final TransactionSystemException transactionSystemException = assertThrows(
                TransactionSystemException.class,
                () -> clientService.save(clientWithInvalidEmail));

        final List<String> constraintViolationMessages = ((ConstraintViolationException) transactionSystemException
                .getMostSpecificCause()).getConstraintViolations()
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList());
        assertThat(constraintViolationMessages).containsExactlyInAnyOrder("Please enter a valid email address.");
    }
}
