package com.spring.security.http_basic_in_memory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// configuracion de seguridad usando la nueva forma declarativa con beans
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Define la cadena de filtros de seguridad (SecurityFilterChain)
    // Aqui se especifica:
        //  - que URLs requieren autenticacion y con que rol
        //  - que mecanismo de autenticacion se usa (en este caso HTTP Basic)
    // HTTP Basic: el cliente envia las credenciales codificadas en Base64
    // en el header Authorization de cada request. Es stateless por diseno,
    // no crea sesion HTTP.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                // Los endpoints bajo /public/** no requieren autenticacion
                .requestMatchers("/public/**").permitAll()
                // Los endpoints bajo /admin/** requieren el rol ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Cualquier otro request requiere estar autenticado con cualquier rol
                .anyRequest().authenticated()
            )
            // Habilita HTTP Basic con la configuracion por defecto
            // Customizer.withDefaults() aplica los valores predeterminados de Spring Security
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // define el repositorio de usuarios en memoria
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user123")) // codifica el password con BCrypt
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN", "USER")
                .build();

        // almacena ambos usuarios en memoria
        return new InMemoryUserDetailsManager(user, admin);
    }

    // define el codificador de contrasenas, usnaod BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
