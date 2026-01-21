
# 5. Métodos Personalizados que Devuelven un Solo Objeto con JPA

En esta sección exploramos cómo definir y utilizar métodos personalizados que devuelven **un único resultado** usando `Optional<T>` como tipo de retorno. Esta práctica es útil cuando se espera encontrar **una sola entidad** basada en criterios específicos.

---

## PersonRepository

```java
public interface PersonRepository extends CrudRepository<Person, Long> {

    // Buscar por ID con @Query
    @Query("select p from Person p where p.id = ?1")
    Optional<Person> buscarPorId(Long id);

    // Buscar por nombre exacto
    @Query("select p from Person p where p.name = ?1")
    Optional<Person> buscarPorNombre(String name);

    // Buscar por coincidencia parcial
    @Query("select p from Person p where p.name like %?1%")
    Optional<Person> findOneLikeName(String name);

    // Alternativa basada en convención
    Optional<Person> findByNameContaining(String name);
}
```

---

## Formas de acceder al resultado de `Optional<Person>`

```java
@Override
public void run(String... args) throws Exception {
    findOne();
}

public void findOne() {

    // 1️⃣ Usar orElseThrow()
    Person person = personRepository.findById(1L).orElseThrow();
    System.out.println("Persona encontrada: " + person);

    // 2️⃣ Validación manual con isPresent()
    Optional<Person> personOptional = personRepository.findById(2L);
    if (personOptional.isPresent()) {
        Person person2 = personOptional.get();
        System.out.println("Persona encontrada: " + person2);
    }

    // 3️⃣ Uso directo con ifPresent()
    personRepository.findById(3L).ifPresent(System.out::println);

    // 4️⃣ Con método personalizado buscarPorId
    Optional<Person> personOptional2 = personRepository.buscarPorId(4L);
    if (personOptional2.isPresent()) {
        System.out.println("Persona encontrada: " + personOptional2.get());
    } else {
        System.out.println("No se ha encontrado la persona");
    }

    // 5️⃣ Con método buscarPorNombre
    Optional<Person> personOptional3 = personRepository.buscarPorNombre("Felipe");
    if (personOptional3.isEmpty()) {
        System.out.println("No se ha encontrado la persona");
    } else {
        System.out.println("Persona encontrada: " + personOptional3.get());
    }

    // 6️⃣ Con like + ifPresentOrElse
    personRepository.findOneLikeName("Em")
        .ifPresentOrElse(
            System.out::println,
            () -> System.out.println("No se ha encontrado la persona")
        );

    personRepository.findOneLikeName("lia")
        .ifPresentOrElse(
            System.out::println,
            () -> System.out.println("No se ha encontrado la persona")
        );

    // 7️⃣ Equivalente con findByNameContaining
    personRepository.findByNameContaining("Jh")
        .ifPresentOrElse(
            System.out::println,
            () -> System.out.println("No se ha encontrado la persona")
        );
}
```

---

## Resultados esperados (consola)

```bash
Persona encontrada: Person{id=1, name=Felipe, ...}
Persona encontrada: Person{id=2, name=Jhonny, ...}
Person{id=3, name=Emilia, ...}
Persona encontrada: Person{id=4, name=Diego, ...}
Persona encontrada: Person{id=1, name=Felipe, ...}
Person{id=3, name=Emilia, ...}
Person{id=3, name=Emilia, ...}
Person{id=2, name=Jhonny, ...}
```

---

## ¿Por qué usar `Optional<Person>`?

* Evita `NullPointerException`.
* Obliga a manejar el caso en que no se encuentre el registro.
* Permite el uso de métodos funcionales como:

  * `ifPresent()`
  * `orElse()`
  * `orElseThrow()`
  * `ifPresentOrElse()`

---

## Conclusión

El uso de métodos personalizados que retornan un solo resultado con `Optional<T>` en JPA es altamente recomendable para garantizar un código más seguro, legible y funcional. Se puede lograr con convenciones de nombre o con anotaciones `@Query`, manteniendo la flexibilidad y control sobre las consultas.
