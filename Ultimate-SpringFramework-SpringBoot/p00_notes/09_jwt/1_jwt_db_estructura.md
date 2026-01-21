# 1. Estructura de la Base de Datos para Autenticación JWT

En este proyecto, se utiliza una base de datos relacional para almacenar la información de usuarios, roles y productos. Esta estructura permite implementar autenticación y autorización mediante Spring Security con JWT, asignando uno o varios roles a cada usuario.

---

## Tablas Principales

### Tabla `users`

Contiene la información básica de cada usuario del sistema.

```sql
CREATE TABLE users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(18) NOT NULL,
  password VARCHAR(60) NOT NULL,
  enable BOOLEAN NOT NULL,
  PRIMARY KEY (id)
);
```

* `username`: nombre único de usuario.
* `password`: contraseña encriptada.
* `enable`: indica si el usuario está habilitado o no.
* Índice único para garantizar usuarios únicos:

  ```sql
  ALTER TABLE users ADD UNIQUE INDEX username_UNIQUE (username ASC) VISIBLE;
  ```

---

### Tabla `roles`

Define los distintos **roles de acceso** (por ejemplo, `ROLE_USER`, `ROLE_ADMIN`).

```sql
CREATE TABLE roles (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL,
  PRIMARY KEY (id)
);
```

* `name`: nombre único del rol (e.g., `ROLE_ADMIN`).
* Índice único:

  ```sql
  ALTER TABLE roles ADD UNIQUE INDEX name_UNIQUE (name ASC) VISIBLE;
  ```

---

### Tabla `users_roles`

Es una tabla de relación **muchos a muchos** entre `users` y `roles`.

```sql
CREATE TABLE users_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id)
);
```

* Se definen claves foráneas para mantener integridad referencial:

```sql
ALTER TABLE users_roles 
ADD CONSTRAINT FK_users FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE users_roles 
ADD CONSTRAINT FK_roles FOREIGN KEY (role_id) REFERENCES roles(id);
```

---

## Tabla Adicional: `products`

Aunque no forma parte del sistema de autenticación, la tabla `products` se usa para pruebas de acceso y restricciones por rol.

```sql
CREATE TABLE products (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(45),
  price INT,
  description TEXT,
  PRIMARY KEY (id)
);

ALTER TABLE products ADD COLUMN sku VARCHAR(45);
```

---

## Observaciones

* El sistema se basa en una arquitectura **relacional normalizada**.
* La relación entre usuarios y roles permite una gestión flexible de permisos.
* El campo `enable` puede usarse para activar/desactivar cuentas sin eliminarlas.
* Las tablas tienen claves primarias simples y compuestas según el caso.

---

## Conclusión

Esta estructura de base de datos proporciona una base sólida para implementar **autenticación y autorización** con JWT en Spring Security. Gracias a la tabla intermedia `users_roles`, es posible manejar múltiples roles por usuario, lo cual resulta ideal para sistemas con jerarquía o permisos personalizados.
