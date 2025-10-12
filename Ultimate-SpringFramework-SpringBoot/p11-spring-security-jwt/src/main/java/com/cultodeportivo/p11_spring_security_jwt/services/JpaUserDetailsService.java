package com.cultodeportivo.p11_spring_security_jwt.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cultodeportivo.p11_spring_security_jwt.entities.User;
import com.cultodeportivo.p11_spring_security_jwt.repositories.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOp = userRepository.findByUsername(username);
        if (userOp.isEmpty()){
            throw new UsernameNotFoundException(String.format("Usuario '%s' no encontrado", username) );
        }
        User user = userOp.orElseThrow();

        List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());


        return new org.springframework.security.core.userdetails.User(user.getUsername(), 
        user.getPassword(), 
        user.getEnable(), 
        true, 
        true, 
        true,
        authorities);
    }
    
}
