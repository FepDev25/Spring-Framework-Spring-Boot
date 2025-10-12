package com.cultodeportivo.p10_springboot_crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cultodeportivo.p10_springboot_crud.entities.User;
import com.cultodeportivo.p10_springboot_crud.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
   
    @Autowired
    private UserRepository repository;

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    @Override
    public User save(User user) {
       return repository.save(user);
    }

    @Transactional
    @Override
    public Optional<User> update(Long id, User user) {
        Optional<User> userOp = repository.findById(id);

        if (userOp.isPresent()) {
            User userDb = userOp.orElseThrow();
            userDb.setName(user.getName());
            userDb.setLastname(user.getLastname());
            userDb.setEmail(user.getEmail());
            userDb.setBirthdate(user.getBirthdate());
            return Optional.of(repository.save(userDb));
        }

        return userOp;
    }

    @Transactional
    @Override
    public Optional<User> delete(Long id) {
        Optional<User> userOp = repository.findById(id);
        userOp.ifPresent(userDb -> repository.delete(userDb));
        return userOp;
    }
}
