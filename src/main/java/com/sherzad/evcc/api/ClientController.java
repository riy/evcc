package com.sherzad.evcc.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.sherzad.evcc.domain.Client;
import com.sherzad.evcc.domain.ClientStatusEnum;
import com.sherzad.evcc.service.ClientService;
import com.sherzad.evcc.support.RestUtils;
import com.sherzad.evcc.web.ClientResource;
import com.sherzad.evcc.web.ClientResourceAssembler;
import com.sherzad.evcc.web.ClientSearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientResourceAssembler clientResourceAssembler;

    @GetMapping("/clients")
    public ResponseEntity<Page<ClientResource>> getAllClients(final Pageable pageable) {
        final Page<Client> clients = clientService.findAll(pageable);
        final List<ClientResource> clientResources = clients.get()
                .map(client -> clientResourceAssembler.toResource(client))
                .collect(Collectors.toList());
        return restUtils.response(new PageImpl<>(clientResources, clients.getPageable(), clients.getSize()));
    }

    @PostMapping("/clients")
    public ResponseEntity<Page<ClientResource>> getClients(
            @RequestBody final ClientSearch clientSearch,
            final Pageable pageable) {
        final Page<Client> clients = clientService.findByClientSearch(clientSearch, pageable);
        final List<ClientResource> clientResources = clients.get()
                .map(client -> clientResourceAssembler.toResource(client))
                .collect(Collectors.toList());
        return restUtils.response(new PageImpl<>(clientResources, clients.getPageable(), clients.getSize()));
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<ClientResource> getClient(@PathVariable final String id) {
        final Optional<Client> client = clientService.findById(id);
        return restUtils.response(clientResourceAssembler.toResource(client.orElse(null)));
    }

    @PostMapping("/client")
    public ResponseEntity<ClientResource> createClient(@Valid @RequestBody final ClientResource clientResource) {
        if (!isEmpty(clientResource.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "When creating a new Client the ID may not be specified.");
        }

        return createOrUpdate(clientResource);
    }

    @PutMapping("/client")
    public ResponseEntity<ClientResource> updateClient(@Valid @RequestBody final ClientResource clientResource) {
        if (isEmpty(clientResource.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please specify the ID of the client to update.");
        }

        return createOrUpdate(clientResource);
    }

    @PutMapping("/client/status/{status}/{id}")
    public ResponseEntity<ClientResource> setClientStatus(@PathVariable final String id, @PathVariable final String status) {
        if (isEmpty(id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please specify the ID of the client to change the status for.");
        }
        if (isEmpty(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please specify the new status of the client.");
        }

        final Optional<Client> optionalClientStatusToUpdate = clientService.findById(id);
        if (optionalClientStatusToUpdate.isEmpty()) {
            return restUtils.response(null);
        }

        final Client clientStatusToUpdate = optionalClientStatusToUpdate.get();
        clientStatusToUpdate.setStatus(ClientStatusEnum.byIdentifier(status));
        final Client updatedClient = clientService.save(clientStatusToUpdate);

        return restUtils.response(clientResourceAssembler.toResource(updatedClient));
    }

    @DeleteMapping("/client/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable final String id) {
        if (isEmpty(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please specify the ID of the client to delete.");
        }

        final Optional<Client> clientToDelete = clientService.findById(id);
        if (clientToDelete.isEmpty()) {
            return restUtils.response(null);
        }

        clientService.deleteById(id);
        return restUtils.emptyResponse();
    }

    private ResponseEntity<ClientResource> createOrUpdate(final ClientResource clientResource) {
        final Client createdOrUpdatedClient = clientService.save(clientResourceAssembler.toModel(clientResource));
        return restUtils.response(clientResourceAssembler.toResource(createdOrUpdatedClient));
    }
}
