package com.cultodeportivo.p4_springoot_error;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cultodeportivo.p4_springoot_error.models.domain.Role;
import com.cultodeportivo.p4_springoot_error.models.domain.User;

@Configuration
public class AppConfig {
    

    @Bean
    @SuppressWarnings("unused")
    List<User> users() {
        return List.of(
            new User(1L, "Felipe", "Peralta", new Role("ADMIN")),   
            new User(2L, "Jane", "Doe", null),
            new User(3L, "John", "Doe", new Role("USER")),
            new User(4L, "Pedro", "Doe", null)
        );
    }
}
