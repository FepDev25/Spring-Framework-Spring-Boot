# 13. Uso de la Palabra Clave `between` en Consultas JPA

La palabra clave `between` en JPA permite **filtrar registros que se encuentren dentro de un rango definido**. Es especialmente útil para trabajar con valores numéricos, fechas y cadenas ordenadas alfabéticamente.

---

## Métodos en el Repositorio

```java
public interface PersonRepository extends CrudRepository<Person, Long> {

    // Por convención
    List<Person> findByIdBetween(Long id1, Long id2);

    @Query("select p from Person p where p.id between ?1 and ?2")
    List<Person> findByIdBetweenMio(Long id1, Long id2);

    List<Person> findByNameBetween(String c1, String c2);

    @Query("select p from Person p where p.name between ?1 and ?2")
    List<Person> findByNameBetweenMio(String c1, String c2);
}
```

> Nota: Ambos enfoques (nombre de método o `@Query`) son válidos y producen el mismo resultado.

---

## Ejecución en la Clase Principal

```java
public void ejemplosPalabrasClave() {

    List<Person> people = personRepository.findByIdBetween(1L, 5L);
    System.out.println("Personas con id entre 1 y 5:");
    people.forEach(System.out::println);

    List<Person> people2 = personRepository.findByIdBetweenMio(1L, 5L);
    System.out.println("Personas con id entre 1 y 5 (query):");
    people2.forEach(System.out::println);

    List<Person> people3 = personRepository.findByNameBetween("E", "J");
    System.out.println("Personas con nombre entre E y J:");
    people3.forEach(System.out::println);

    List<Person> people4 = personRepository.findByNameBetweenMio("E", "J");
    System.out.println("Personas con nombre entre E y J (query):");
    people4.forEach(System.out::println);
}
```

---

## Resultados esperados

```text
Personas con id entre 1 y 5:
Felipe, Jhonny, Emilia, Diego, Nube

Personas con nombre entre E y J:
Felipe, Emilia, Gustavo
```

> Nota: En el caso de cadenas (`String`), `between` realiza una **comparación lexicográfica**. Por ejemplo, `"E"` y `"J"` incluye nombres como `Emilia`, `Felipe`, `Gustavo`.

---

## Comparación entre métodos

| Método                      | Tipo       | Comentario                                   |
| --------------------------- | ---------- | -------------------------------------------- |
| `findByIdBetween(...)`      | Convención | Generado automáticamente por Spring          |
| `findByIdBetweenMio(...)`   | `@Query`   | Más explícito, útil para personalizar lógica |
| `findByNameBetween(...)`    | Convención | Ordena cadenas por orden alfabético          |
| `findByNameBetweenMio(...)` | `@Query`   | Misma lógica pero usando JPQL manual         |

---

## Recomendaciones

* Usa `between` cuando necesites filtrar datos entre rangos claros de fechas, IDs o nombres.
* En cadenas de texto, verifica el orden alfabético según lo esperado (puede depender del motor de base de datos).
* Para mayor claridad, agrega ordenamientos si es necesario: `order by p.name`.

---

## Conclusión

`between` es una herramienta poderosa en JPA para realizar consultas por rangos. Puedes implementarla fácilmente usando **nombres de métodos** o directamente con **JPQL** mediante `@Query`, obteniendo flexibilidad y expresividad en tus consultas.
