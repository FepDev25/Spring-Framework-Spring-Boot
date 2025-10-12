package com.cultodeportivo.p4_springoot_error.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cultodeportivo.p4_springoot_error.models.domain.User;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private List<User> users;

    public UserServiceImp() {
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
       return users.stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    
}
