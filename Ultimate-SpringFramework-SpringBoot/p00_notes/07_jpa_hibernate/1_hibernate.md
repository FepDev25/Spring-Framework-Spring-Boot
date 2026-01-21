# 1. Teoría: JPA e Hibernate

## ¿Qué es Hibernate?

Hibernate es un **framework de mapeo objeto-relacional (ORM)** que permite a los desarrolladores de Java interactuar con bases de datos relacionales utilizando objetos y clases en lugar de sentencias SQL tradicionales.

- Cada clase Java representa una tabla en la base de datos.
- Cada instancia de una clase representa una fila (registro).
- Cada atributo de clase se corresponde con una columna.
- Simplifica las operaciones CRUD y la persistencia de objetos Java.

### Ejemplo de entidad

```java
@Entity
public class User {
    @Id
    private Long id;

    @Column(name = "user_name")
    private String name;

    private String email;
    private String password;

    // Getters y Setters
}
```

## ¿Qué es JPA?

**JPA (Java Persistence API)** es una **especificación estándar de Java** que define una API para el manejo, almacenamiento y recuperación de datos entre objetos Java y una base de datos relacional.

- JPA no es una implementación, sino un conjunto de **interfaces y anotaciones**.
- Hibernate es una de las **implementaciones más populares** de JPA.
- Otros proveedores JPA incluyen: EclipseLink, OpenJPA, TopLink.

---

## Diferencias entre JPA e Hibernate

| Característica       | JPA                                          | Hibernate                                      |
|----------------------|----------------------------------------------|------------------------------------------------|
| Naturaleza           | API/Especificación                           | Framework/Implementación                       |
| Propósito            | Define contratos y anotaciones estándar      | Provee una implementación concreta de JPA      |
| Portabilidad         | Alta (funciona con cualquier proveedor)      | Menor (acoplado a Hibernate)                   |
| Funciones exclusivas | Limitado a lo definido por la especificación | Ofrece muchas extensiones avanzadas propias    |
| Comunidad y soporte  | Estándar oficial de Java EE/Jakarta EE       | Amplia comunidad, documentación y plugins      |
| Lenguaje de consulta | JPQL (Java Persistence Query Language)       | HQL (Hibernate Query Language), Criteria, SQL  |

> Nota: Hibernate **implementa** JPA, pero también extiende sus funcionalidades. Cuando usamos JPA con Hibernate, estamos utilizando la API estándar con el motor potente de Hibernate.

---

## Tipos de consultas en Hibernate

1. **HQL (Hibernate Query Language)**

   - Similar a SQL, pero opera sobre objetos y atributos.
   - Independiente del tipo de base de datos.

2. **Criteria API**

   - Construye consultas de forma **programática y dinámica**.
   - Evita errores de sintaxis de cadenas de texto.

3. **SQL Nativo**

   - Permite ejecutar consultas SQL tradicionales directamente sobre la base de datos.

---

## Asociaciones entre Entidades

Hibernate (y JPA) permiten mapear relaciones entre entidades usando anotaciones específicas:

- `@OneToOne`: Un objeto se asocia con uno y solo uno.
- `@OneToMany`: Un objeto tiene múltiples asociados.
- `@ManyToOne`: Muchos objetos se asocian con uno.
- `@ManyToMany`: Relación de muchos a muchos.

> Nota: Estas anotaciones permiten reflejar estructuras relacionales complejas directamente en el modelo de objetos de Java.

---

## Conclusión

JPA e Hibernate son fundamentales en aplicaciones Java modernas que requieren persistencia de datos. Mientras que JPA define una forma estándar y desacoplada de manejar la persistencia, Hibernate ofrece una implementación robusta y con muchas características avanzadas que facilitan el trabajo del desarrollador.
