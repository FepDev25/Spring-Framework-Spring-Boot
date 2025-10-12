package com.cultodeportivo.p10_springboot_crud.repositories;

import org.springframework.data.repository.CrudRepository;

import com.cultodeportivo.p10_springboot_crud.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
    
}
