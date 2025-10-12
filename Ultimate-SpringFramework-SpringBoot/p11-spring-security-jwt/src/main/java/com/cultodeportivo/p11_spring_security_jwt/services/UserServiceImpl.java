package com.cultodeportivo.p11_spring_security_jwt.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cultodeportivo.p11_spring_security_jwt.entities.Role;
import com.cultodeportivo.p11_spring_security_jwt.entities.User;
import com.cultodeportivo.p11_spring_security_jwt.repositories.RoleRepository;
import com.cultodeportivo.p11_spring_security_jwt.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleService; 

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Transactional
    @Override
    public User save(User user) {

        Optional<Role> roleUserOP = roleService.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        
        roleUserOP.ifPresent(roles::add);

        if (user.isAdmin()) {
            Optional<Role> roleAdminOP = roleService.findByName("ROLE_ADMIN");
            roleAdminOP.ifPresent(roles::add);
        }

        user.setRoles(roles);

        String passwordEncoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordEncoded);

        return userRepository.save(user);
    }


    @Transactional(readOnly = true)
    @Override
    public boolean existsByUsername(String username) {
        boolean exists = userRepository.existsByUsername(username);
        System.out.println("¿Existe username '" + username + "'?: " + exists);
        return exists;
    }


    

}
