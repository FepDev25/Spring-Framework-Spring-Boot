package com.cultodeportivo.p11_spring_security_jwt.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cultodeportivo.p11_spring_security_jwt.entities.User;
import static com.cultodeportivo.p11_spring_security_jwt.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.cultodeportivo.p11_spring_security_jwt.security.TokenJwtConfig.PREFIX_TOKEN;
import static com.cultodeportivo.p11_spring_security_jwt.security.TokenJwtConfig.SECRET_KEY;
import static com.cultodeportivo.p11_spring_security_jwt.security.TokenJwtConfig.CONTENT_TYPE;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        
        User user;
        String username = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();
        } catch (StreamReadException e) {
            System.out.println(e);
        } catch (DatabindException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        String username = user.getUsername();
        Collection<? extends GrantedAuthority> roles =  authResult.getAuthorities();

        // Claims claims = Jwts.claims()
        //     .add("authorities", new ObjectMapper().writeValueAsString(roles))
        //     .build();

        Claims claims2 = Jwts.claims()
        .add("authorities", roles.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()))
        .build();

        String token = Jwts.builder()
            .subject(username)
            // .claims(claims)
            .claims(claims2)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 3600000))
            .signWith(SECRET_KEY)
            .compact();

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token); 

        Map<String, String> body = new HashMap<>();
        body.put("token", token);
        body.put("username", username);
        body.put("message", String.format("Hola %s, te has logueado con éxito", username));

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(200);
        response.setContentType(CONTENT_TYPE);
        
    }
    
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        
        Map<String, String> body = new HashMap<>();
        body.put("message", "El usuario o la contraseña son incorrectos");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType(CONTENT_TYPE);
    }
}
