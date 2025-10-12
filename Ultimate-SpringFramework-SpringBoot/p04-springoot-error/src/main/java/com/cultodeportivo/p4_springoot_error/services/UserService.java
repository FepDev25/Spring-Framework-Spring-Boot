package com.cultodeportivo.p4_springoot_error.services;

import java.util.List;
import java.util.Optional;

import com.cultodeportivo.p4_springoot_error.models.domain.User;

public interface UserService {

    List<User> findAll();
    Optional<User> findById(Long id);
}
