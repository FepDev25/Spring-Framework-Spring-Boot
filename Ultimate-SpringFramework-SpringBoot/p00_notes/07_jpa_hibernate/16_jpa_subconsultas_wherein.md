# 16. Subconsultas y `WHERE IN` en JPQL

JPQL permite usar subconsultas y expresiones como `IN` para construir **consultas más potentes y expresivas**, similares al SQL tradicional. Son útiles para comparar valores con listas o calcular resultados basados en agregaciones.

---

## Métodos en el Repositorio

```java
public interface PersonRepository extends CrudRepository<Person, Long> {

    // WHERE IN: Filtrar por lista de IDs
    @Query("select p from Person p where p.id IN ?1")
    List<Person> getPersonsById(List<Long> ids);

    // SUBCONSULTA: Nombres más cortos
    @Query("select p.name, length(p.name) from Person p where length(p.name) = ( select min(length(p2.name)) from Person p2 )")
    List<Object[]> shorterNames();

    // SUBCONSULTA: Última persona registrada
    @Query("select p from Person p where p.id = ( select max(p2.id) from Person p2 )")
    Optional<Person> getLastRegistration();
}
```

---

## Ejecución de los métodos

```java
public void ejemploSubconsultas() {

    // Subconsulta: nombres más cortos
    List<Object[]> shorterNames = personRepository.shorterNames();
    System.out.println("Nombres más cortos:");
    shorterNames.forEach(arr -> System.out.println(arr[0] + " - " + arr[1]));

    // Subconsulta: último registro
    Optional<Person> lastRegistration = personRepository.getLastRegistration();
    if (lastRegistration.isPresent()) {
        System.out.println("Última persona registrada: " + lastRegistration.get());
    } else {
        System.out.println("No hay personas registradas.");
    }

    // Where IN: buscar por varios IDs
    List<Person> people = personRepository.getPersonsById(List.of(1L, 2L, 3L));
    System.out.println("Personas con ID 1, 2 o 3:");
    people.forEach(System.out::println);
}
```

---

## Ejemplo de salida

```text
Nombres más cortos:
Ana - 3
Sol - 3

Última persona registrada:
Person{id=21, name=Oswaldinho, lastName=Bonilla, programmingLanguage=Python}

Personas con ID 1, 2 o 3:
Person{id=1, name=Felipe, ...}
Person{id=2, name=Jhonny, ...}
Person{id=3, name=Emilia, ...}
```

---

## ¿Qué es `WHERE IN`?

* Permite filtrar registros cuyos valores coincidan con **una lista** de valores.
* Equivale a: `WHERE columna IN (valor1, valor2, valor3...)`.
* En JPQL, se puede pasar una lista como parámetro y Spring se encarga de expandirla.

---

## ¿Qué es una subconsulta?

* Una consulta **anidada dentro de otra**.
* Se ejecuta primero para producir un resultado, que luego se usa en la consulta externa.
* Puede ir en cláusulas `WHERE`, `SELECT`, `HAVING`, etc.

---

## Buenas prácticas

| Práctica                      | Recomendación                                       |
| ----------------------------- | --------------------------------------------------- |
| `IN ?1`                       | Usar listas de tamaño moderado                      |
| Subconsultas con `max`, `min` | Útiles para obtener valores extremos o relacionados |
| Validación con `Optional`     | Siempre validar si el resultado está presente       |

---

## Conclusión

Tanto las subconsultas como `WHERE IN` amplían enormemente el poder de JPQL, permitiéndote construir queries complejos sin salir de Spring Data. Son especialmente útiles para reportes, filtros por listas, y lógica dependiente de cálculos agregados.
