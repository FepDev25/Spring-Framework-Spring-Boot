# 17. Ciclo de Vida de Entidades: `@PrePersist` y `@PreUpdate`

En JPA, el ciclo de vida de una entidad puede interceptarse usando **callbacks**. Las anotaciones `@PrePersist` y `@PreUpdate` permiten ejecutar lógica justo **antes de insertar** o **antes de actualizar** un registro en la base de datos.

---

## Anotaciones

| Anotación      | Momento de ejecución                        |
|----------------|---------------------------------------------|
| `@PrePersist`  | Antes de insertar una nueva entidad         |
| `@PreUpdate`   | Antes de actualizar una entidad existente   |

---

## Clase `Person` con manejo de fechas

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

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    // Constructores, Getters y Setters omitidos para brevedad

    @PrePersist
    public void prePersist() {
        System.out.println("PrePersist: Preparing to save a new Person entity");
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    public void preUpdate() {
        System.out.println("PreUpdate: Preparing to update a Person entity");
        this.updatedAt = LocalDate.now();
    }
}
```

---

## Flujo en `CommandLineRunner`

```java
@Override
public void run(String... args) throws Exception {
    // crearConEntrada(); // guarda y ejecuta PrePersist
    // updateVersionUno(); // modifica y ejecuta PreUpdate
    list(); // imprime todas las entidades
}
```

---

## Ejemplo de salida

```text
PrePersist: Preparing to save a new Person entity
PreUpdate: Preparing to update a Person entity

Person{id=22, name=Jaime, lastName=Ulloa, programmingLanguage=Python, createdAt=2025-05-26, updatedAt=2025-05-26}
```

> Nota: `createdAt` se asigna automáticamente cuando se guarda una entidad por primera vez.
> Nota: `updatedAt` se actualiza automáticamente cada vez que se modifica y persiste la entidad.

---

## Ventajas de usar `@PrePersist` y `@PreUpdate`

* Automatiza el manejo de fechas de creación y modificación.
* Evita olvidar campos importantes al guardar o actualizar.
* Mantiene el código del servicio más limpio y enfocado.

---

## Notas

* Puedes usar otras anotaciones del ciclo de vida como `@PostPersist`, `@PreRemove`, `@PostUpdate`, etc.
* Los métodos deben ser `void`, sin argumentos y tener cualquier nombre válido.
* No deben lanzar excepciones checked.

---

## Conclusión

El uso de `@PrePersist` y `@PreUpdate` es una técnica muy útil para gestionar automáticamente información relacionada con el ciclo de vida de las entidades, como fechas de auditoría. Es una forma elegante de mantener las entidades coherentes y listas para entornos de producción.
