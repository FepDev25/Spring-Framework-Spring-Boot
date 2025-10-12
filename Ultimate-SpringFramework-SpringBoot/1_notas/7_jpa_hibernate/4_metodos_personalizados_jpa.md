# 4. Métodos Personalizados en JPA: Derivados vs `@Query`

Spring Data JPA permite crear consultas personalizadas de dos formas principales:

1. A través de nombres de métodos descriptivos (`query method names`).
2. Usando la anotación `@Query` con JPQL.

Este ejemplo demuestra ambos enfoques sobre la entidad `Person`.

---

## 📁 Repositorio: `PersonRepository`

```java
public interface PersonRepository extends CrudRepository<Person, Long> {

    List<Person> findByProgrammingLanguage(String programmingLanguage);

    @Query("select p from Person p where p.programmingLanguage = ?1")
    List<Person> buscarByProgrammingLanguage(String programmingLanguage);

    List<Person> findByProgrammingLanguageAndName(String programmingLanguage, String name);

    @Query("select p from Person p where p.programmingLanguage = ?1 and p.name = ?2")
    List<Person> buscarByProgrammingLanguageAndName(String programmingLanguage, String name);
}
```

### 🔍 Análisis de los métodos

| Método                                    | Tipo de definición    | Descripción                                                    |
| ----------------------------------------- | --------------------- | -------------------------------------------------------------- |
| `findByProgrammingLanguage(...)`          | Método derivado       | Busca por atributo directamente usando convención de nombres.  |
| `buscarByProgrammingLanguage(...)`        | Consulta con `@Query` | Equivalente al anterior, pero escrito con JPQL explícitamente. |
| `findByProgrammingLanguageAndName(...)`   | Método derivado       | Busca por dos atributos al mismo tiempo.                       |
| `buscarByProgrammingLanguageAndName(...)` | Consulta con `@Query` | Equivalente a la anterior con sintaxis JPQL manual.            |

---

## 🧪 Clase principal: Ejecución y resultados

```java
@Override
public void run(String... args) throws Exception {

    System.out.println("Listado de personas: ");
    personRepository.findAll().forEach(System.out::println);

    System.out.println("Personas con lenguaje Java:");
    personRepository.findByProgrammingLanguage("Java").forEach(System.out::println);

    System.out.println("Personas con lenguaje Java (con @Query):");
    personRepository.buscarByProgrammingLanguage("Java").forEach(System.out::println);

    System.out.println("Java y nombre Felipe:");
    personRepository.findByProgrammingLanguageAndName("Java", "Felipe").forEach(System.out::println);

    System.out.println("Java y nombre Felipe (con @Query):");
    personRepository.buscarByProgrammingLanguageAndName("Java", "Felipe").forEach(System.out::println);
}
```

---

## 🧾 Resultados en consola

```
Listado de personas:
Person{id=1, name=Felipe, lastName=Peralta, programmingLanguage=Java}
Person{id=2, name=Jhonny, lastName=Segarra, programmingLanguage=JavaScript}
...

Personas con lenguaje Java:
Hibernate: SELECT ... WHERE programming_language = ?
→ Felipe, Karen

Personas con lenguaje Java (con @Query):
Hibernate: SELECT ... WHERE programming_language = ?
→ Felipe, Karen

Java y nombre Felipe:
Hibernate: SELECT ... WHERE programming_language = ? AND name = ?
→ Felipe

Java y nombre Felipe (con @Query):
Hibernate: SELECT ... WHERE programming_language = ? AND name = ?
→ Felipe
```

---

## 🧠 ¿Cuál usar: Derivado vs `@Query`?

| Opción            | Ventajas                                       | Cuándo usar                                   |
| ----------------- | ---------------------------------------------- | --------------------------------------------- |
| Método derivado   | Rápido, limpio, sin necesidad de escribir JPQL | Para consultas simples y directas             |
| `@Query` con JPQL | Mayor control, más expresivo y flexible        | Para consultas más complejas o personalizadas |

> 📌 En ambos casos, Spring se encarga de generar la consulta adecuada usando Hibernate como motor ORM.

---

## ✅ Conclusión

Spring Data JPA permite declarar consultas complejas de manera intuitiva, ya sea usando la **convención de nombres** o expresando directamente la lógica con `@Query`. Ambas opciones son válidas, y su elección depende de la complejidad de la lógica y la claridad deseada.

