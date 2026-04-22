package com.spring.security.form_login_sessions.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// Configuracion de seguridad para autenticacion con formulario HTML
/*
tras autenticarse exitosamente, el servidor crea una sesion HTTP y devuelve una cookie JSESSIONID al cliente. 
En cada request siguiente el cliente envia esa cookie y Spring Security recupera el contexto de autenticacion 
de la sesion, sin necesidad de re-enviar credenciales.
*/
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                // URL donde Spring Security redirige al usuario no autenticado
                .loginPage("/login")
                .defaultSuccessUrl("/home", true) // URL de redireccion tras login exitoso
                .failureUrl("/login?error") // URL de redireccion cuando las credenciales son incorrectas
                .permitAll() // Permite acceso sin autenticacion a todas las URLs de login
            )

            .logout(logout -> logout
                // URL que procesa el logout
                .logoutUrl("/logout")
                // Redireccion tras logout exitoso
                .logoutSuccessUrl("/login?logout")
                // Invalida la sesion HTTP del servidor
                .invalidateHttpSession(true)
                // Elimina la cookie de sesion del navegador del cliente
                .deleteCookies("JSESSIONID")
            )

            /*
            Cuando el usuario marca el checkbox "Recordarme", Spring Security genera una cookie llamada "remember-me" 
            con el siguiente contenido:
                base64(username + ":" + expirationTime + ":" +
                       md5Hex(username + ":" + expirationTime + ":" +
                              password + ":" + key))
             
            Cuando la sesion expira y el usuario vuelve con la cookie
            remember-me valida, RememberMeAuthenticationFilter re-autentica
            al usuario sin que tenga que volver a ingresar credenciales.
            */
            .rememberMe(remember -> remember
                // Clave secreta usada para el HMAC del token (en produccion debe venir de configuracion externa)
                .key("uniqueAndSecretKey")
                // Duracion del token en segundo
                .tokenValiditySeconds(86400)
            )

            .sessionManagement(session -> session
                // Limita a una sesion activa por usuario
                .maximumSessions(1)
            );

        return http.build();
    }

    // bean de usuarios en memoria para autenticacion
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user123"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN", "USER")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
