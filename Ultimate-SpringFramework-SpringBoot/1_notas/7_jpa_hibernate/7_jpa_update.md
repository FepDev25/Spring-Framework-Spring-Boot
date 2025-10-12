# 7. Actualización de Registros en JPA con `@Transactional`

Spring Data JPA permite actualizar entidades fácilmente utilizando `save(...)`. Si el objeto ya existe (es decir, tiene un `id` válido), la operación de `save()` actualiza los datos en la base de datos. Este ejemplo muestra cómo hacerlo interactuando desde la consola, usando dos variantes.

---

## 🔄 ¿Por qué `@Transactional`?

La anotación `@Transactional` asegura que la operación de actualización esté encapsulada en una **transacción completa**, lo cual permite el rollback en caso de error.

```java
@Transactional
public void update() {
    updateVersionDos();
}
```

---

## 🧪 Versión 1: Actualización directa con `orElseThrow()`

```java
public void updateVersionUno() {
    try (Scanner scanner = new Scanner(System.in)) {
        List<Person> people = (List<Person>) personRepository.findAll();
        System.out.println("Listado de personas:");
        people.forEach(System.out::println);

        System.out.println("Introduce el id de la persona a modificar:");
        Long id = scanner.nextLong();
        scanner.nextLine(); // Consumir salto de línea

        System.out.println("Introduce el nuevo nombre:");
        String name = scanner.nextLine();

        System.out.println("Introduce el nuevo apellido:");
        String lastName = scanner.nextLine();

        System.out.println("Introduce el nuevo lenguaje de programación:");
        String programmingLanguage = scanner.nextLine();

        Person person = personRepository.findById(id).orElseThrow();
        person.setName(name);
        person.setLastName(lastName);
        person.setProgrammingLanguage(programmingLanguage);

        Person personNew = personRepository.save(person);
        System.out.println("Persona actualizada: " + personNew);
    }
}
```

---

## ✅ Versión 2: Con `Optional.ifPresentOrElse(...)`

```java
public void updateVersionDos() {
    try (Scanner scanner = new Scanner(System.in)) {
        List<Person> people = (List<Person>) personRepository.findAll();
        System.out.println("Listado de personas:");
        people.forEach(System.out::println);

        System.out.println("Introduce el id de la persona a modificar:");
        Long id = scanner.nextLong();
        scanner.nextLine();

        System.out.println("Introduce el nuevo nombre:");
        String name = scanner.nextLine();

        System.out.println("Introduce el nuevo apellido:");
        String lastName = scanner.nextLine();

        System.out.println("Introduce el nuevo lenguaje de programación:");
        String programmingLanguage = scanner.nextLine();

        Optional<Person> person = personRepository.findById(id);
        person.ifPresentOrElse(
            p -> {
                p.setName(name);
                p.setLastName(lastName);
                p.setProgrammingLanguage(programmingLanguage);
                Person personNew = personRepository.save(p);
                System.out.println("Persona actualizada: " + personNew);
            },
            () -> System.out.println("No se ha encontrado la persona")
        );
    }
}
```

---

## 🧾 Ejemplo en consola

```
Introduce el id de la persona a modificar: 
10
Introduce el nuevo nombre de la persona: 
Santiago
Introduce el nuevo apellido de la persona: 
Larrea
Introduce el nuevo lenguaje de programacion de la persona: 
PHP
Hibernate: select ... from persons where id=?
Hibernate: update persons set ... where id=?
Persona actualizada: Person{id=10, name=Santiago, lastName=Larrea, programmingLanguage=PHP}
```

---

## 🧠 Consideraciones

* `save(...)` actualiza automáticamente si el ID existe, de lo contrario crea un nuevo registro.
* Siempre es recomendable validar la existencia con `findById(...)`.
* `@Transactional` no es obligatorio si `save()` se llama desde métodos públicos gestionados por Spring, pero asegura consistencia si se combina con otras operaciones.

---

## ✅ Conclusión

Actualizar entidades con JPA es directo y seguro si se maneja correctamente el `Optional`, la entrada del usuario y se encapsula la operación en una transacción. La variante con `ifPresentOrElse` es más segura y expresiva para evitar excepciones innecesarias.
