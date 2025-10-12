package com.cultodeportivo.p11_spring_security_jwt.services;

import java.util.List;

import com.cultodeportivo.p11_spring_security_jwt.entities.User;

public interface UserService {

    List<User> findAll();
    User save(User user);
    boolean existsByUsername(String username);
}
