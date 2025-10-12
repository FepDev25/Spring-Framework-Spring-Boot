# 3. Ejemplo Básico de JPA con Spring Boot

Este ejemplo demuestra cómo crear una aplicación simple en Spring Boot usando JPA para conectarse a una base de datos MySQL, mapear una entidad, y listar sus datos en consola.

---

## 🧱 Estructura del Proyecto

- `Person`: Entidad JPA mapeada a la tabla `persons`.
- `PersonRepository`: Repositorio que hereda de `CrudRepository`.
- `P8SprinbootJpaApplication`: Clase principal que ejecuta la lógica al iniciar.

---

## 📁 Entidad: `Person`

```java
@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastName;

    @Column(name = "programming_language")
    private String programmingLanguage;

    // Constructores, Getters, Setters y toString()
}
```

### 🔍 Explicación:

* `@Entity`: Marca esta clase como entidad persistente.
* `@Table(name = "persons")`: Mapea la clase a la tabla `persons`.
* `@Id`: Indica el atributo que actúa como clave primaria.
* `@GeneratedValue(...)`: Auto-genera el valor del `id`.
* `@Column(name = "programming_language")`: Mapea el campo `programmingLanguage` a una columna personalizada.

---

## 📁 Repositorio: `PersonRepository`

```java
public interface PersonRepository extends CrudRepository<Person, Long> {
}
```

### ✔ Ventajas:

* Al extender `CrudRepository`, se obtienen métodos como `findAll()`, `save()`, `deleteById()` sin necesidad de implementarlos.

---

## 🚀 Clase Principal: `P8SprinbootJpaApplication`

```java
@SpringBootApplication
public class P8SprinbootJpaApplication implements CommandLineRunner {

    @Autowired
    private PersonRepository personRepository;

    public static void main(String[] args) {
        SpringApplication.run(P8SprinbootJpaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<Person> people = (List<Person>) personRepository.findAll();
        System.out.println("Listado de personas: ");
        people.stream().forEach(System.out::println);
    }
}
```

### 🧠 ¿Qué hace?

* Usa `CommandLineRunner` para ejecutar código justo después del arranque.
* Inyecta `PersonRepository` con `@Autowired`.
* Recupera todas las personas de la base de datos y las imprime en consola.

---

## ⚙️ Configuración en `application.properties`

```properties
spring.application.name=p8-sprinboot-jpa

spring.datasource.url=jdbc:mysql://localhost:3306/db_springboot
spring.datasource.username=root
spring.datasource.password=xxxxx
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```

### 🔍 Claves:

* `spring.datasource.url`: URL de conexión a la base de datos.
* `spring.jpa.hibernate.ddl-auto=update`: Crea o actualiza automáticamente la tabla según la entidad.
* `spring.jpa.show-sql=true`: Muestra las consultas SQL generadas en consola.

---

## ✅ Conclusión

Este ejemplo básico cubre todo lo necesario para empezar con JPA en Spring Boot:

* Mapeo de entidades.
* Repositorios sin necesidad de implementación.
* Configuración mínima.
* Ejecución automática de lógica tras el arranque.

Es ideal como base para proyectos más complejos que requieran persistencia con bases de datos relacionales.

