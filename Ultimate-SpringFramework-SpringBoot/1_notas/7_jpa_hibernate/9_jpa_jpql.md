# 9. Consultas Personalizadas con JPQL en Spring Data JPA

JPQL (Java Persistence Query Language) es un lenguaje de consultas orientado a **entidades Java**, no a tablas de bases de datos directamente. Spring Data JPA permite utilizar JPQL a través de la anotación `@Query`.

Este subtema muestra varios ejemplos de cómo extraer campos específicos o múltiples campos desde una entidad llamada `Person`.

---

## 🔍 Consultas definidas en el repositorio

```java
public interface PersonRepository extends CrudRepository<Person, Long> {

    // Consultas personalizadas con JPQL

    @Query("select p.name from Person p where p.id = ?1")
    String getNameById(Long id); 

    @Query("select concat(p.name, ' ', p.lastName) from Person p where p.id = ?1")
    String getFullNameById(Long id); 

    @Query("select p.id from Person p where p.id = ?1")
    Long getIdById(Long id); 

    @Query("select p.name, p.programmingLanguage, p.lastName from Person p")
    List<Object[]> obtenerPersonDataFull();

    @Query("select p.name, p.lastName, p.programmingLanguage from Person p where p.id = ?1")
    Object obtenerPersonDataFull(Long id);
}
```

---

## 🧪 Ejecución desde la clase principal

```java
@Transactional(readOnly = true)
public void ejemplosJPQL() {

    Long id = 1L;

    String name = personRepository.getNameById(2L);
    System.out.println("Nombre de la persona con id " + id + ": " + name);

    Long retornoId = personRepository.getIdById(id);
    System.out.println("Id de la persona con id " + id + ": " +  retornoId);

    String fullName = personRepository.getFullNameById(id);
    System.out.println("Nombre completo de la persona con id " + id + ": " + fullName);

    Object[] personData = (Object[]) personRepository.obtenerPersonDataFull(id);
    System.out.println("Datos de la persona con id " + id + ": " + personData[0] + " - " + personData[1] + " - " + personData[2]);

    List<Object[]> peopleData = personRepository.obtenerPersonDataFull();
    System.out.println("Datos de todas las personas:");
    peopleData.forEach(arr -> System.out.println(arr[0] + " - " + arr[1] + " - " + arr[2]));
}
```

---

## 🧾 Ejemplo de salida esperada

```
Hibernate: select p1_0.name from persons p1_0 where p1_0.id=?
Nombre de la persona con id 1: Jhonny

Hibernate: select p1_0.id from persons p1_0 where p1_0.id=?
Id de la persona con id 1: 1

Hibernate: select concat(p1_0.name,' ',p1_0.last_name) from persons p1_0 where p1_0.id=?
Nombre completo de la persona con id 1: Felipe Peralta

Hibernate: select p1_0.name,p1_0.last_name,p1_0.programming_language from persons p1_0 where p1_0.id=?
Datos de la persona con id 1: Felipe - Peralta - Java

Hibernate: select p1_0.name,p1_0.programming_language,p1_0.last_name from persons p1_0
Datos de todas las personas:
Felipe - Java - Peralta
Jhonny - Go - Segarra Lopez
Emilia - Python - Bonilla
...
```

---

## 💡 ¿Cuándo usar JPQL?

* Cuando necesitas extraer solo ciertos campos de una entidad.
* Cuando deseas evitar cargar la entidad completa (`SELECT p`).
* Cuando necesitas **proyecciones personalizadas**.
* Para cálculos o transformaciones como `concat(...)`, `count(...)`, etc.

---

## ✅ Conclusión

JPQL permite ejecutar consultas orientadas a objetos sobre entidades persistentes. Combinado con `@Query`, ofrece una forma flexible y potente de personalizar las consultas en Spring Data JPA, especialmente útil para obtener subconjuntos de datos sin necesidad de mapear DTOs directamente.
