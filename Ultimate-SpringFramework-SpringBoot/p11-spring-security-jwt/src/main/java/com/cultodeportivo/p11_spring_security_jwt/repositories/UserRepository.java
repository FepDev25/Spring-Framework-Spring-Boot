package com.cultodeportivo.p11_spring_security_jwt.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.cultodeportivo.p11_spring_security_jwt.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

}
