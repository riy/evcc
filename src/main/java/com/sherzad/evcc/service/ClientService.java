package com.sherzad.evcc.service;

import java.util.Optional;

import com.sherzad.evcc.domain.Client;
import com.sherzad.evcc.domain.ClientStatusEnum;
import com.sherzad.evcc.repository.ClientRepository;
import com.sherzad.evcc.web.ClientSearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public Page<Client> findAll(final Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Client> findAllByStatusActive(final Pageable pageable) {
        return clientRepository.findAllByStatus(pageable, ClientStatusEnum.ACTIVE);
    }

    @Transactional(readOnly = true)
    public Page<Client> findByClientSearch(final ClientSearch clientSearch, final Pageable pageable) {
        // The query generated here will be ANDed for any parameter that is not null. It should be part of a custom Repository
        // implementation in a more complex environment.
        final ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        final Example<Client> query = Example.of(
                Client.builder()
                        .lastname(clientSearch.getLastname())
                        .zipCode(clientSearch.getZipCode())
                        .city(clientSearch.getCity())
                        .country(clientSearch.getCountry())
                        .status(ClientStatusEnum.ACTIVE)
                        .build(),
                matcher);
        return clientRepository.findAll(query, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Client> findById(final String id) {
        return clientRepository.findById(id);
    }

    @Transactional
    public Client save(final Client client) {
        return clientRepository.save(client);
    }

    @Transactional
    public void deleteById(final String id) {
        clientRepository.deleteById(id);
    }
}
