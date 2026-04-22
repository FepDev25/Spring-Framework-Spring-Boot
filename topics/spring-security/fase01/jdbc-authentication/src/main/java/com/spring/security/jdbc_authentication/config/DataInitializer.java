package com.spring.security.jdbc_authentication.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Component;

// Inicializa los usuarios de prueba en la base de datos al arrancar la aplicacion
@Component
public class DataInitializer implements ApplicationRunner {

    private final JdbcUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(JdbcUserDetailsManager userDetailsManager,
                           PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    // metodo run() se ejecuta una vez que el contexto de Spring esta completamente inicializado y la aplicacion 
    // lista pararecibir requests
    @Override
    public void run(ApplicationArguments args) {
        crearUsuarioSiNoExiste("user", "user123", "ROLE_USER");
        crearUsuarioSiNoExiste("admin", "admin123", "ROLE_ADMIN", "ROLE_USER");
    }

    // createUser() de JdbcUserDetailsManager ejecuta internamente dos INSERTs:
        // INSERT INTO users (username, password, enabled) VALUES (?, ?, ?)
        // INSERT INTO authorities (username, authority) VALUES (?, ?)  <- por cada authority
    private void crearUsuarioSiNoExiste(String username, String password, String... roles) {
        if (!userDetailsManager.userExists(username)) {
            UserDetails nuevoUsuario = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .authorities(roles)
                    .build();
            userDetailsManager.createUser(nuevoUsuario);
        }
    }
}
