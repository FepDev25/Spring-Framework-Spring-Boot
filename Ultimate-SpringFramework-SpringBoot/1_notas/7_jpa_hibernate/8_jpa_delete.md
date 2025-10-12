# 8. Eliminación de Registros con JPA (`deleteById` y `delete`)

Eliminar entidades en JPA es tan sencillo como usar los métodos `deleteById(ID)` o `delete(T entity)`. En este ejemplo interactivo, se muestra cómo eliminar una persona por su ID ingresado desde consola.

---

## 🔄 ¿Por qué usar `@Transactional`?

La anotación `@Transactional` permite que las operaciones de eliminación se ejecuten dentro de una **transacción controlada**, garantizando que si ocurre un error, los cambios se revierten.

```java
@Transactional
public void delete() {
    //deleteVersionUno();
    deleteVersionDos();
}
```

---

## 🧪 Versión 1: `deleteById(...)` (rápido y directo)

```java
public void deleteVersionUno() {
    try (Scanner scanner = new Scanner(System.in)) {
        List<Person> people = (List<Person>) personRepository.findAll();
        System.out.println("Listado de personas:");
        people.forEach(System.out::println);

        System.out.println("Introduce el id de la persona a eliminar:");
        Long id = scanner.nextLong();
        scanner.nextLine();

        personRepository.deleteById(id);
        System.out.println("Persona eliminada");
    }
}
```

### ✔ Características

* Elimina directamente con el ID.
* No valida si el registro existe previamente.
* Puede lanzar `EmptyResultDataAccessException` si no se encuentra el ID.

---

## ✅ Versión 2: `delete(...)` con validación

```java
public void deleteVersionDos() {
    try (Scanner scanner = new Scanner(System.in)) {
        List<Person> people = (List<Person>) personRepository.findAll();
        System.out.println("Listado de personas:");
        people.forEach(System.out::println);

        System.out.println("Introduce el id de la persona a eliminar:");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Person> person = personRepository.findById(id);
        person.ifPresentOrElse(
            p -> {
                personRepository.delete(p);
                System.out.println("Persona eliminada: " + p);
            },
            () -> System.out.println("No se ha encontrado la persona")
        );
    }
}
```

### ✔ Características

* Verifica primero si la persona existe.
* Evita errores si el ID no corresponde a ningún registro.
* Proporciona retroalimentación clara al usuario.

---

## 🧾 Ejemplo en consola

```
Introduce el id de la persona a eliminar: 
10
Hibernate: select ... from persons where id=?
Hibernate: delete from persons where id=?
Persona eliminada: Person{id=10, name=Santiago, lastName=Larrea, programmingLanguage=PHP}
```

---

## 💡 Diferencias clave

| Método           | Ventaja                         | Riesgo potencial                          |
| ---------------- | ------------------------------- | ----------------------------------------- |
| `deleteById(id)` | Rápido y directo                | Puede lanzar excepción si no existe       |
| `delete(entity)` | Más control y validación previa | Necesita una consulta previa (`findById`) |

---

## ✅ Conclusión

La eliminación de registros en Spring Data JPA es flexible y segura si se implementa correctamente. La segunda variante con `Optional` y `ifPresentOrElse` es preferible en aplicaciones interactivas o críticas, ya que evita errores y mejora la experiencia del usuario.
