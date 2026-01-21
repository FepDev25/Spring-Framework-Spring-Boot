# 6. Crear Registros con JPA usando `@Transactional`

Spring Data JPA permite guardar nuevas entidades en la base de datos usando el método `save(...)`. Este ejemplo muestra cómo crear un registro estático y otro usando entrada por consola, dentro de un método anotado con `@Transactional`.

---

## ¿Por qué usar `@Transactional`?

La anotación `@Transactional` garantiza que todas las operaciones de persistencia dentro del método se ejecuten en una única **transacción atómica**. Si algo falla, se realiza un **rollback** automático.

```java
@Transactional
public void create() {
    crearEstatico();
    crearConEntrada();
}
```

---

## Creación de Persona Estática

```java
public void crearEstatico() {
    Person person1 = new Person(null, "Lionel", "Messi", "PySoccer");
    Person personNew = personRepository.save(person1);
    System.out.println("Persona creada: " + personNew);
}
```

* Se crea una nueva instancia de `Person`.
* `save(...)` guarda la entidad y retorna el objeto persistido (con `id` generado).
* Se imprime el objeto creado.

---

## Creación con Entrada por Consola

```java
public void crearConEntrada() {
    try (Scanner scanner = new Scanner(System.in)) {
        System.out.println("Introduce el nombre de la persona: ");
        String name = scanner.nextLine();
        
        System.out.println("Introduce el apellido de la persona: ");
        String lastName = scanner.nextLine();
        
        System.out.println("Introduce el lenguaje de programación de la persona: ");
        String programmingLanguage = scanner.nextLine();

        Person person2 = new Person(null, name, lastName, programmingLanguage);
        Person personNew2 = personRepository.save(person2);

        System.out.println("Persona creada: " + personRepository.findById(personNew2.getId()).orElseThrow());
    }
}
```

* Usa `Scanner` para ingresar datos desde el usuario.
* Crea y guarda un nuevo objeto `Person`.
* Vuelve a consultar la entidad desde la base de datos para mostrarla confirmando su creación.

---

## Resultado en consola

```bash
Introduce el nombre de la persona: 
Carlos
Introduce el apellido de la persona: 
Jimenez
Introduce el lenguaje de programacion de la persona: 
Go
Hibernate: insert into persons (last_name,name,programming_language) values (?,?,?)
Hibernate: select p1_0.id,p1_0.last_name,p1_0.name,p1_0.programming_language from persons p1_0 where p1_0.id=?
Persona creada: Person{id=12, name=Carlos, lastName=Jimenez, programmingLanguage=Go}
```

---

## Buenas prácticas

* Anota métodos de escritura (`save`, `delete`) con `@Transactional`.
* Valida la entrada del usuario antes de persistir.
* Usa `orElseThrow()` al recuperar entidades para detectar errores de persistencia.

---

## Conclusión

Crear entidades con JPA es directo y eficiente. El uso de `@Transactional` asegura que las operaciones sean coherentes y seguras. Ya sea con datos estáticos o dinámicos, `save(...)` se encarga de insertar o actualizar registros automáticamente.
