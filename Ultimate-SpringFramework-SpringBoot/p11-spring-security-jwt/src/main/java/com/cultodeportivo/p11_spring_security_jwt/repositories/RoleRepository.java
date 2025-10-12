package com.cultodeportivo.p11_spring_security_jwt.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.cultodeportivo.p11_spring_security_jwt.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
