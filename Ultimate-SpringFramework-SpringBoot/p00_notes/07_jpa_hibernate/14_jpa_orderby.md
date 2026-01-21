# 14. Uso de la Palabra Clave `Order By` en JPA

La cláusula `ORDER BY` en JPQL o en los métodos derivados de Spring Data JPA permite ordenar los resultados de las consultas según uno o más atributos de la entidad.

---

## Métodos en el Repositorio

```java
public interface PersonRepository extends CrudRepository<Person, Long> {

    // JPQL explícito con @Query
    @Query("select p from Person p order by p.lastName")
    List<Person> findAllOrderByLastNameMio();

    @Query("select p from Person p order by p.lastName desc")
    List<Person> findAllOrderByLastNameMioDesc();

    @Query("select p from Person p order by p.lastName, p.name")
    List<Person> findAllOrderByLastNameAndNameMio();

    // Derivado con filtro + orden
    List<Person> findByNameBetweenOrderByNameDesc(String c1, String c2);

    // Derivado: orden ascendente por nombre
    List<Person> findAllByOrderByName();
}
```

> Nota: Puedes usar tanto `@Query` como métodos construidos por convención para ordenar resultados.

---

## Ejecución de ejemplos en `CommandLineRunner`

```java
public void ejemplosPalabraClaveOrderBy() {

    List<Person> people = personRepository.findAllOrderByLastNameMio();
    System.out.println("Personas ordenadas por apellido (asc):");
    people.forEach(System.out::println);

    List<Person> people2 = personRepository.findAllOrderByLastNameMioDesc();
    System.out.println("Personas ordenadas por apellido (desc):");
    people2.forEach(System.out::println);

    List<Person> people3 = personRepository.findAllOrderByLastNameAndNameMio();
    System.out.println("Personas ordenadas por apellido y nombre:");
    people3.forEach(System.out::println);

    List<Person> people4 = personRepository.findByNameBetweenOrderByNameDesc("E", "J");
    System.out.println("Personas con nombre entre E y J (orden descendente):");
    people4.forEach(System.out::println);

    List<Person> people5 = personRepository.findAllByOrderByName();
    System.out.println("Personas ordenadas por nombre (ascendente):");
    people5.forEach(System.out::println);
}
```

---

## Ejemplos de salida esperada

```text
Personas ordenadas por apellido (asc):
Emilia Bonilla
Lionel Messi
...
Jhonny Segarra Lopez

Personas ordenadas por apellido (desc):
Jhonny Segarra Lopez
...
Emilia Bonilla

Personas ordenadas por apellido y nombre:
Bonilla - Emilia
Messi - Lionel
Peralta - Diego
...

Personas con nombre entre E y J (desc):
Gustavo Portilla
Felipe Peralta
Emilia Bonilla

Personas ordenadas por nombre:
Diego, Emilia, Felipe, Gustavo, ...
```

---

## Ventajas de `ORDER BY` en JPA

* Mejora la presentación de los datos.
* Útil en listas alfabéticas, reportes, dashboards y páginas web.
* Combinable con filtros (`where`) y rangos (`between`).

---

## Sintaxis general

| Forma                         | Descripción                                      |
| ----------------------------- | ------------------------------------------------ |
| `findAllByOrderByCampoAsc()`  | Orden ascendente (por defecto)                   |
| `findAllByOrderByCampoDesc()` | Orden descendente                                |
| `@Query(... order by ...)`    | Consulta JPQL personalizada con múltiples campos |

---

## Conclusión

El uso de `ORDER BY` permite definir cómo se ordenan los resultados al consultarlos desde la base de datos. Spring Data JPA lo admite tanto por **convención en el nombre del método** como por **JPQL en consultas personalizadas**, dándote flexibilidad para adaptar la salida a los requerimientos de tu aplicación.
