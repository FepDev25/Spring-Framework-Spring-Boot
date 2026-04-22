package com.spring.security.jdbc_authentication.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// Configuracion de seguridad con autenticacion respaldada por base de datos JDBC
// Spring Security consulta la base de datos en cada autenticacion usando JdbcUserDetailsManager.
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Publico: login, registro y la consola H2 (solo desarrollo)
                .requestMatchers("/login", "/register", "/h2-console/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )

            //Configuracion especial para la consola H2 en desarrollo, configuracion es exclusiva para desarrollo.
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
            );

        return http.build();
    }

    // JdbcUserDetailsManager como UserDetailsService
    // Esta clase implementa tanto UserDetailsService como UserDetailsManager
        // UserDetailsService: solo lectura -> loadUserByUsername()
        // UserDetailsManager: lectura y escritura -> createUser(), updateUser(), deleteUser(), changePassword(), userExists()
    // JdbcUserDetailsManager espera que la base de datos tenga las tablas "users" y "authorities" con 
    // el esquema exacto de Spring Security
    @Bean
    public JdbcUserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
