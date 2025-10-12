# 18. Uso de `@Embeddable` y `@Embedded` para Reutilizar Campos en JPA

Cuando varias entidades comparten atributos comunes (como fechas de auditoría), es buena práctica **extraer esos campos a una clase reutilizable**. JPA permite esto con:

- `@Embeddable`: Marca una clase cuyos campos serán "embebidos" en otra entidad.
- `@Embedded`: Se utiliza en la entidad principal para incluir el objeto embebido.

---

## 🧱 Clase `Audit` como embeddable

```java
@Embeddable
public class Audit {

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

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

    // Getters y Setters
}
```

### 🔍 Ventajas

* Reutilización de lógica y campos comunes.
* Mismos callbacks (`@PrePersist`, `@PreUpdate`) funcionan correctamente.
* Código más limpio y modular.

---

## 📁 Entidad `Person` usando `@Embedded`

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

    @Embedded
    private Audit audit = new Audit();

    // Constructores, Getters y Setters

    @Override
    public String toString() {
        String createdAt = audit != null ? audit.getCreatedAt().toString() : "sin fecha";
        String updatedAt = audit != null ? audit.getUpdatedAt().toString() : "sin fecha";

        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", programmingLanguage='" + programmingLanguage + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
```

> 📌 El objeto `audit` no se guarda como entidad separada, sino como **campos integrados** en la tabla `persons`.

---

## 🧪 Ejemplo de ejecución

```text
PreUpdate: Preparing to update a Person entity

Persona actualizada:
Person{id=24, name=Jhoel, lastName=Paida, programmingLanguage=Go,
createdAt=Creado en: 2025-05-26,
updatedAt=Actualizado en: 2025-05-26}
```

---

## 📌 Consideraciones

| Aspecto             | Detalle                                                      |
| ------------------- | ------------------------------------------------------------ |
| Persistencia        | Los campos embebidos se almacenan en la misma tabla          |
| Callbacks (`@PreX`) | Funcionan igual que en la entidad principal                  |
| Inicialización      | Se recomienda instanciar el objeto embebido al declararlo    |
| Reutilización       | Puede ser usada en múltiples entidades (`User`, `Post`, etc) |

---

## ✅ Conclusión

El uso de `@Embeddable` y `@Embedded` permite **centralizar y reutilizar atributos comunes** en las entidades JPA, manteniendo un código más limpio y modular. Es ideal para patrones de auditoría, direcciones, coordenadas, o cualquier agrupación de campos lógica.