# 12. Funciones JPQL: `concat`, `lower`, `upper` en Consultas Personalizadas

JPQL (Java Persistence Query Language) admite varias funciones similares a SQL, entre ellas:

- `concat(...)`: Concatenar cadenas de texto.
- `lower(...)`: Convertir texto a minúsculas.
- `upper(...)`: Convertir texto a mayúsculas.

Este ejemplo muestra cómo usar estas funciones para construir nombres completos o transformar los datos de salida.

---

## PersonRepository

```java
public interface PersonRepository extends CrudRepository<Person, Long> {

    @Query("select concat(p.name, ' ', p.lastName) from Person p")
    List<String> findAllFullNameConcat(); 

    @Query("select p.name || ' ' || p.lastName from Person p")
    List<String> findAllFullNameConcat2(); 

    @Query("select lower(concat(p.name, ' ', p.lastName)) from Person p")
    List<String> findAllFullNameLower(); 

    @Query("select upper(p.name || ' ' || p.lastName) from Person p")
    List<String> findAllFullNameUpper(); 

    @Query("select upper(p.name), upper(p.lastName), lower(p.programmingLanguage) from Person p")
    List<Object[]> findAllFullNameConcatObject(); 
}
```

---

## Ejecución de las consultas

```java
public void ejemplosJPQLFunctions() {

    List<String> fullNames = personRepository.findAllFullNameConcat();
    System.out.println("Nombres completos de las personas:");
    fullNames.forEach(System.out::println);

    List<String> fullNames2 = personRepository.findAllFullNameConcat2();
    System.out.println("Nombres completos (concat2):");
    fullNames2.forEach(System.out::println);

    List<String> fullNamesLower = personRepository.findAllFullNameLower();
    System.out.println("Nombres en minúsculas:");
    fullNamesLower.forEach(System.out::println);

    List<String> fullNamesUpper = personRepository.findAllFullNameUpper();
    System.out.println("Nombres en mayúsculas:");
    fullNamesUpper.forEach(System.out::println);

    List<Object[]> fullNamesObject = personRepository.findAllFullNameConcatObject();
    System.out.println("Transformaciones combinadas:");
    fullNamesObject.forEach(arr -> System.out.println(arr[0] + " - " + arr[1] + " - " + arr[2]));
}
```

---

## Resultados esperados (extracto)

```text
Nombres completos de las personas:
Felipe Peralta
Jhonny Segarra Lopez
...

Nombres completos (concat2):
Felipe Peralta
...

Nombres en minúsculas:
felipe peralta
...

Nombres en mayúsculas:
FELIPE PERALTA
...

Transformaciones combinadas:
FELIPE - PERALTA - java
JHONNY - SEGARRA LOPEZ - go
...
```

---

## ¿Por qué usar estas funciones?

| Función  | Uso principal                                                          |
| -------- | ---------------------------------------------------------------------- |
| `concat` | Unir columnas de texto en una sola cadena                              |
| `lower`  | Convertir a minúsculas (útil para búsquedas o normalización visual)    |
| `upper`  | Convertir a mayúsculas (útil para estandarizar visualmente o comparar) |

---

## Consideraciones

- Las funciones JPQL dependen del dialecto de la base de datos (por ejemplo, `||` para concatenación puede no funcionar en todos los motores, usar `concat()` es más seguro).
- El uso de funciones puede hacer que la consulta sea solo de lectura (no apta para escritura).

---

## Conclusión

El uso de funciones de cadena en JPQL permite **personalizar el formato de los resultados directamente desde la consulta**, evitando tener que transformar los datos manualmente en Java. Esto es útil para informes, exportaciones o estandarización de datos desde la base.
