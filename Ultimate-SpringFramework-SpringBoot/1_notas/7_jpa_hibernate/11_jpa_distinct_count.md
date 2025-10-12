# 11. Uso de `distinct` y `count` en JPQL

JPQL permite usar funciones como `distinct` y `count` para realizar consultas más precisas y obtener estadísticas sobre los datos. Este subtema explora cómo usarlas para evitar repeticiones y contar elementos únicos.

---

## 📁 Repositorio: Consultas JPQL

```java
@Query("select p.name from Person p")
List<String> findAllNames();

@Query("select distinct(p.name) from Person p")
List<String> findAllNamesDistinct();

@Query("select distinct(p.programmingLanguage) from Person p")
List<String> findAllProgrammingLanguageDistinct();

@Query("select count(distinct(p.programmingLanguage)) from Person p")
Long findAllProgrammingLanguageCount();
```

---

## 🧪 Método de ejecución: `ejemplosJPQLDistinct()`

```java
public void ejemplosJPQLDistinct() {

    List<String> names = personRepository.findAllNames();
    System.out.println("Nombres de las personas:");
    names.forEach(System.out::println);

    List<String> distinctNames = personRepository.findAllNamesDistinct();
    System.out.println("Nombres distintos:");
    distinctNames.forEach(System.out::println);

    List<String> distinctProgrammingLanguages = personRepository.findAllProgrammingLanguageDistinct();
    System.out.println("Lenguajes de programación distintos:");
    distinctProgrammingLanguages.forEach(System.out::println);

    Long programmingLanguageCount = personRepository.findAllProgrammingLanguageCount();
    System.out.println("Cantidad de lenguajes de programación distintos: " + programmingLanguageCount);
}
```

---

## 🔍 Descripción de las funciones

### `distinct`

* Elimina elementos duplicados del resultado.
* Se puede aplicar a cualquier campo.
* Muy útil para mostrar listas sin repeticiones (como nombres o lenguajes).

```java
@Query("select distinct(p.name) from Person p")
```

### `count(distinct ...)`

* Cuenta cuántos elementos distintos hay en una columna.
* Se usa comúnmente para obtener estadísticas rápidas.

```java
@Query("select count(distinct(p.programmingLanguage)) from Person p")
```

---

## 🧾 Resultado esperado (consola)

```
Nombres de las personas:
Felipe
Jhonny
Emilia
Diego
...
Felipe
Jhonny
...

Nombres distintos:
Felipe
Jhonny
Emilia
...

Lenguajes de programación distintos:
Java
Go
Python
Rust
...

Cantidad de lenguajes de programación distintos: 6
```

---

## ✅ Conclusión

El uso de `distinct` y `count` en JPQL permite:

* Evitar duplicación en resultados.
* Extraer valores únicos de una columna.
* Obtener estadísticas con facilidad.

Estas funciones son ideales para dashboards, filtros, o informes que requieren datos únicos o agregados.