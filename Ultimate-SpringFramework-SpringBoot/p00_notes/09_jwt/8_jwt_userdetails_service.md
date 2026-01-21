# 8. Cargar Usuario por Nombre de Usuario – `UserDetailsService`

En una aplicación segura con JWT, es fundamental contar con una manera de **cargar los datos del usuario autenticado**. Spring Security utiliza la interfaz `UserDetailsService` para buscar usuarios por su nombre (`username`) y validar sus credenciales. Esta funcionalidad es clave para generar el token JWT y verificar su validez en futuras solicitudes.

---

## Objetivo

Implementar un servicio que:

- Busque un usuario en la base de datos.
- Cargue sus roles y estado.
- Devuelva un objeto `UserDetails` compatible con Spring Security.

---

## Clase: `JpaUserDetailsService`

```java
@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOp = userRepository.findByUsername(username);
        if (userOp.isEmpty()){
            throw new UsernameNotFoundException(String.format("Usuario '%s' no encontrado", username));
        }

        User user = userOp.orElseThrow();

        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.getEnable(), // enabled
            true,  // accountNonExpired
            true,  // credentialsNonExpired
            true,  // accountNonLocked
            authorities
        );
    }
}
```

---

## Explicación Paso a Paso

| Paso | Acción                                                                                                                                       |
| ---- | -------------------------------------------------------------------------------------------------------------------------------------------- |
| 1    | Se busca al usuario por su `username` usando el `UserRepository`.                                                                            |
| 2    | Si no se encuentra, se lanza una excepción `UsernameNotFoundException`.                                                                      |
| 3    | Se extraen los roles del usuario (`List<Role>`) y se transforman en `GrantedAuthority`.                                                      |
| 4    | Se retorna una instancia de `UserDetails`, implementada por la clase interna de Spring `org.springframework.security.core.userdetails.User`. |

---

## ¿Qué es `UserDetails`?

Es una interfaz que Spring usa para representar al usuario autenticado. Contiene:

- Nombre de usuario
- Contraseña (ya encriptada)
- Estado de la cuenta
- Roles (como `ROLE_USER`, `ROLE_ADMIN`)

---

## Detalles sobre `GrantedAuthority`

```java
List<GrantedAuthority> authorities = user.getRoles().stream()
    .map(role -> new SimpleGrantedAuthority(role.getName()))
    .collect(Collectors.toList());
```

- `GrantedAuthority` representa los **permisos o roles** del usuario.
- `SimpleGrantedAuthority` toma el nombre del rol (por ejemplo: `"ROLE_USER"`).
- Es crucial para que el `SecurityContext` pueda evaluar si el usuario tiene acceso a ciertas rutas.

---

## ¿Por qué `UserDetailsService` es importante?

- Es invocado automáticamente por Spring Security en el proceso de login.
- Permite integrar tu sistema de usuarios personalizado con la infraestructura de seguridad.
- Es el **punto de partida** para emitir un JWT válido que luego será firmado y enviado al cliente.

---

## Conclusión

La clase `JpaUserDetailsService` conecta la lógica de negocio con el núcleo de Spring Security, permitiendo cargar usuarios desde la base de datos y convertirlos en objetos `UserDetails`. Esta implementación es esencial para validar credenciales y emitir tokens JWT válidos y seguros.
