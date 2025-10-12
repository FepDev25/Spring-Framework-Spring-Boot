package com.cultodeportivo.p10_springboot_crud.services;

import java.util.List;
import java.util.Optional;

import com.cultodeportivo.p10_springboot_crud.entities.User;

public interface UserService {
    
    List<User> findAll();
    Optional<User> findById(Long id);
    User save(User user);
    Optional<User> update(Long id, User user);
    Optional<User> delete(Long id);
}
