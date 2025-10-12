package com.cultodeportivo.p9_springboot_jpa_relationship.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cultodeportivo.p9_springboot_jpa_relationship.entities.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {

    @Query("SELECT c FROM Client c left join fetch c.addresses left join fetch c.invoices left join fetch c.clientDetails WHERE c.id = ?1")
    Optional<Client> findOne(Long id);

}
