# 15. Funciones de Agregación en JPQL

Las funciones de agregación permiten realizar cálculos directamente sobre los datos en la base, como contar registros, calcular promedios, obtener máximos y mínimos, etc. JPQL incluye las funciones típicas de SQL:

- `count()`
- `min()`
- `max()`
- `sum()`
- `avg()`

---

## 🧩 Métodos en el Repositorio

```java
public interface PersonRepository extends CrudRepository<Person, Long> {

    @Query("select count(p) from Person p")
    Long totalPersons();

    @Query("select count(p.id) from Person p where p.programmingLanguage = ?1")
    Long totalPersonsByProgrammingLanguage(String programmingLanguage);

    @Query("select max(p.id) from Person p")
    Long maxId();

    @Query("select min(p.id) from Person p")
    Long minId();

    @Query("select p.name, length(p.name) from Person p")
    List<Object[]> findNameAndLength();

    @Query("select min(length(p.name)) from Person p")
    Integer getMinLengthName();

    @Query("select max(length(p.name)) from Person p")
    Integer getMaxLengthName();

    @Query("select avg(length(p.name)) from Person p")
    Double getAvgLengthName();

    @Query("select min(p.id), max(p.id), sum(p.id), avg(length(p.name)), count(p.id) from Person p")
    Object getResumeAgregationFunction();
}
```

---

## 🚀 Llamadas desde el `CommandLineRunner`

```java
public void ejemplosFuncionesAgregacion() {

    Long totalPersons = personRepository.totalPersons();
    System.out.println("Total de personas: " + totalPersons);

    Long totalGo = personRepository.totalPersonsByProgrammingLanguage("Go");
    System.out.println("Total de personas con Go: " + totalGo);

    System.out.println("Id mínimo: " + personRepository.minId());
    System.out.println("Id máximo: " + personRepository.maxId());

    List<Object[]> nameAndLength = personRepository.findNameAndLength();
    System.out.println("Nombre y longitud:");
    nameAndLength.forEach(arr -> System.out.println(arr[0] + " - " + arr[1]));

    System.out.println("Nombre más corto: " + personRepository.getMinLengthName());
    System.out.println("Nombre más largo: " + personRepository.getMaxLengthName());
    System.out.println("Promedio de longitud de nombres: " + personRepository.getAvgLengthName());

    Object[] resumen = (Object[]) personRepository.getResumeAgregationFunction();
    System.out.println("Resumen:");
    System.out.println("Mínimo ID: " + resumen[0]);
    System.out.println("Máximo ID: " + resumen[1]);
    System.out.println("Suma de IDs: " + resumen[2]);
    System.out.println("Promedio longitud nombre: " + resumen[3]);
    System.out.println("Cantidad total: " + resumen[4]);
}
```

---

## 🧾 Ejemplo de salida

```text
Total de personas: 12
Total de personas con Go: 3
Id mínimo: 1
Id máximo: 21

Nombre y longitud:
Felipe - 6
Jhonny - 6
Emilia - 6
...

Nombre más corto: 3
Nombre más largo: 10
Promedio de longitud de nombres: 5.5833

Resumen:
Mínimo ID: 1
Máximo ID: 21
Suma de IDs: 105
Promedio longitud nombre: 5.5833
Cantidad total: 12
```

---

## 🔍 ¿Cuándo usar funciones de agregación?

| Función    | Uso común                                           |
| ---------- | --------------------------------------------------- |
| `count()`  | Contar registros                                    |
| `sum()`    | Calcular totales (suma de IDs, montos, etc.)        |
| `min()`    | Obtener el valor mínimo de una columna              |
| `max()`    | Obtener el valor máximo                             |
| `avg()`    | Calcular el promedio                                |
| `length()` | Calcular longitud de cadenas (en campos tipo texto) |

---

## ✅ Conclusión

Las funciones de agregación en JPQL te permiten obtener estadísticas y resúmenes directamente desde la base de datos, sin necesidad de procesar todos los datos manualmente en Java. Esto mejora el rendimiento y facilita la construcción de reportes o dashboards.
