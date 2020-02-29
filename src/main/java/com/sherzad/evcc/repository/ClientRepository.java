package com.sherzad.evcc.repository;

import com.sherzad.evcc.domain.Client;
import com.sherzad.evcc.domain.ClientStatusEnum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

    Page<Client> findAllByStatus(final Pageable pageable, final ClientStatusEnum status);

}
