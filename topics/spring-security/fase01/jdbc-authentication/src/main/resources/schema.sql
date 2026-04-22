-- Esquema requerido por Spring Security para JdbcUserDetailsManager.
-- Este es el esquema oficial definido en:
--   spring-security-core -> org/springframework/security/core/userdetails/jdbc/users.ddl
-- Las columnas son exactamente las que JdbcUserDetailsManager espera en sus queries SQL por defecto. 

CREATE TABLE IF NOT EXISTS users (
    -- Clave primaria: el username del usuario
    username  VARCHAR(50)  NOT NULL PRIMARY KEY,
    -- Hash BCrypt del password. Longitud 500 para acomodar cualquier algoritmo futuro.
    -- BCrypt produce hashes de 60 caracteres, pero se usa 500 por convension defensiva.
    password  VARCHAR(500) NOT NULL,
    -- Permite deshabilitar un usuario sin eliminarlo. JdbcUserDetailsManager
    -- excluye usuarios con enabled=false de la autenticacion.
    enabled   BOOLEAN      NOT NULL
);

CREATE TABLE IF NOT EXISTS authorities (
    username  VARCHAR(50) NOT NULL,
    -- El nombre del authority debe incluir el prefijo ROLE_ si se usa hasRole().
    -- Ejemplos validos: ROLE_USER, ROLE_ADMIN, READ_PRIVILEGE, WRITE_PRIVILEGE.
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authorities_users
        FOREIGN KEY (username) REFERENCES users (username)
);

-- Indice unico que previene duplicados de (usuario, authority).
-- Sin este indice, errores en createUser() podrian insertar el mismo rol dos veces.
CREATE UNIQUE INDEX IF NOT EXISTS ix_auth_username
    ON authorities (username, authority);
