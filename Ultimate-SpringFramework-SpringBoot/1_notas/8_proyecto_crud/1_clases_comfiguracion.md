### **1. Clases de Configuración en el Proyecto CRUD con Spring Boot**

En este primer tema, se documentarán las clases de configuración principales que forman parte del proyecto **CRUD con Spring Boot**.

#### **1.1. Clase Principal de la Aplicación (`P10SpringbootCrudApplication`)**

Esta clase es la entrada principal de la aplicación. Utiliza la anotación `@SpringBootApplication`, que habilita la configuración automática de Spring Boot y también realiza un escaneo de los componentes dentro del paquete base para crear el contexto de la aplicación.

**Código:**

```java
package com.cultodeportivo.p10_springboot_crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class P10SpringbootCrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(P10SpringbootCrudApplication.class, args);
    }
}
```

**Descripción:**

* **`@SpringBootApplication`**: Es una combinación de tres anotaciones: `@Configuration`, `@EnableAutoConfiguration`, y `@ComponentScan`. Esto permite que Spring Boot se configure automáticamente, escanee los componentes de la aplicación y realice la inicialización del contexto.
* **`main`**: El método `main` es el punto de entrada de la aplicación. Llama a `SpringApplication.run()`, que arranca la aplicación Spring Boot.

#### **1.2. Clase de Configuración de Propiedades (`AppConfig`)**

La clase `AppConfig` se encarga de definir las configuraciones relacionadas con la carga de propiedades externas, como archivos de propiedades.

**Código:**

```java
package com.cultodeportivo.p10_springboot_crud;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:messages.properties")
public class AppConfig {
    
}
```

**Descripción:**

* **`@Configuration`**: Indica que esta clase contiene configuraciones adicionales para el contexto de la aplicación, como beans, servicios, entre otros.
* **`@PropertySource`**: Permite la carga de un archivo de propiedades en el contexto de Spring. En este caso, se está cargando el archivo `messages.properties`, que contiene mensajes de validación personalizados.

  * **`classpath:messages.properties`**: La ruta especifica que el archivo `messages.properties` se encuentra en el classpath del proyecto, lo que significa que debe ser incluido en los recursos.

#### **1.3. Archivo de Propiedades (`messages.properties`)**

Este archivo de propiedades contiene mensajes utilizados para la validación de los campos en las entidades del proyecto.

**Contenido del archivo `messages.properties`:**

```properties
NotEmpty.product.name=es requerido
NotBlank.product.description=es requerido, por favor
NotNull.product.price=no puede ser nulo, ok
```

**Descripción:**

* **Mensajes de Validación**: Aquí se definen los mensajes que se utilizarán durante la validación de los datos, por ejemplo, cuando un campo esté vacío o nulo. Estos mensajes se asocian con las restricciones de validación de Hibernate y JSR-303 (como `@NotEmpty`, `@NotBlank`, y `@NotNull`).

  * **`NotEmpty.product.name`**: Este mensaje se usará cuando el campo `name` del producto esté vacío.
  * **`NotBlank.product.description`**: Este mensaje se usará cuando el campo `description` del producto esté en blanco.
  * **`NotNull.product.price`**: Este mensaje se usará cuando el campo `price` del producto sea nulo.

#### **1.4. Archivo de Configuración de la Base de Datos (`application.properties`)**

Este archivo contiene la configuración de la base de datos que la aplicación utilizará, así como otras configuraciones importantes para la ejecución de la aplicación.

**Contenido del archivo `application.properties`:**

```properties
spring.application.name=p10-springboot-crud

spring.datasource.url=jdbc:mysql://localhost:3306/db_jpa_crud
spring.datasource.username=root
spring.datasource.password=xxx
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql=true
```

**Descripción:**

* **`spring.application.name`**: Establece el nombre de la aplicación.
* **`spring.datasource.url`**: Define la URL de conexión a la base de datos. En este caso, se conecta a una base de datos MySQL llamada `db_jpa_crud` que se encuentra en el host `localhost`.
* **`spring.datasource.username`**: Especifica el nombre de usuario para la base de datos. En este caso, se utiliza `root`.
* **`spring.datasource.password`**: Establece la contraseña para el usuario de la base de datos. En este caso, se usa `xxx` como contraseña.
* **`spring.datasource.driver-class-name`**: Especifica el controlador de la base de datos, en este caso, MySQL (`com.mysql.cj.jdbc.Driver`).
* **`spring.jpa.show-sql`**: Activa la opción para mostrar las consultas SQL generadas por Hibernate en la consola. Esto es útil para la depuración y monitoreo de las consultas que realiza la aplicación.

### **Resumen de las Clases de Configuración**

1. **Clase Principal (`P10SpringbootCrudApplication`)**: Es el punto de entrada de la aplicación, donde se arranca el contenedor de Spring Boot.
2. **Clase de Configuración (`AppConfig`)**: Carga archivos de propiedades externas, como el archivo `messages.properties`, que contiene los mensajes utilizados en las validaciones.
3. **Archivo `messages.properties`**: Contiene mensajes de validación personalizados utilizados por Hibernate y JSR-303 para proporcionar mensajes en caso de que un campo no cumpla con las restricciones de validación.
4. **Archivo `application.properties`**: Configura la base de datos y otras propiedades clave de la aplicación, como el nombre de la aplicación y la activación de la visualización de SQL.

Estas clases y archivos permiten configurar la aplicación correctamente para interactuar con la base de datos y manejar la validación de datos de manera eficiente.
