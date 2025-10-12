# 10. JPQL Parte 2: Proyecciones Mixtas, Constructores y DTOs

En esta segunda parte del uso de JPQL, se abordan consultas más avanzadas con retornos combinados (`Object[]`), creación de nuevos objetos a través de constructores personalizados y mapeo directo a DTOs (Data Transfer Objects).

---

## 🧩 Consultas en el repositorio

```java
@Query("select p, p.programmingLanguage from Person p")
List<Object[]> findAllMixPerson();

@Query("select new Person(p.name, p.lastName) from Person p")
List<Person> findAllPersonPersonalized();

@Query("select new com.cultodeportivo.p8_sprinboot_jpa.dto.PersonDto(p.name, p.lastName) from Person p")
List<PersonDto> findAllPersonDto();
```

---

## 🧪 Ejecución desde el método `ejemplosJPQL2()`

```java
@Transactional(readOnly = true)
public void ejemplosJPQL2() {

    List<Object[]> personRegs = personRepository.findAllMixPerson();
    System.out.println("Personas con lenguaje de programación:");
    personRegs.forEach(reg -> {
        Person person = (Person) reg[0];
        String programmingLanguage = (String) reg[1];
        System.out.println("Persona: " + person + ", Lenguaje de programación: " + programmingLanguage);
    });

    List<Person> personRegs2 = personRepository.findAllPersonPersonalized();
    System.out.println("Personas con nombre y apellido:");
    personRegs2.forEach(System.out::println);

    List<PersonDto> personRegs3 = personRepository.findAllPersonDto();
    System.out.println("Personas con nombre y apellido (DTO):");
    personRegs3.forEach(System.out::println);
}
```

---

## 🔍 Tipos de consultas y retornos

### 1️⃣ Proyección Mixta (`Object[]`)

Consulta:

```java
@Query("select p, p.programmingLanguage from Person p")
```

* Devuelve una lista de arreglos de objetos (`Object[]`).
* `Object[0]` contiene la entidad `Person`.
* `Object[1]` contiene el valor del campo adicional.

🔎 Resultado:

```
Persona: Person{id=1, name=Felipe, ...}, Lenguaje: Java
```

---

### 2️⃣ Constructor Personalizado con Entidad

Consulta:

```java
@Query("select new Person(p.name, p.lastName) from Person p")
```

* Crea nuevas instancias de `Person` usando solo `name` y `lastName`.
* El resto de atributos (`id`, `programmingLanguage`) quedan `null`.

🔎 Resultado:

```
Person{id=null, name=Felipe, lastName=Peralta, programmingLanguage=null}
```

---

### 3️⃣ Mapeo a DTO

Consulta:

```java
@Query("select new com.cultodeportivo.p8_sprinboot_jpa.dto.PersonDto(p.name, p.lastName) from Person p")
```

* Usa un constructor de la clase `PersonDto` para mapear datos específicos.
* Ideal para exposición a APIs o capas superiores sin filtrar toda la entidad.

🔎 Resultado:

```
PersonDto{name='Felipe', lastName='Peralta'}
```

---

## 📌 Requisitos técnicos

Para que las proyecciones constructoras funcionen correctamente:

* El constructor debe estar definido con los **mismos tipos y orden de argumentos** que en la consulta.
* Para DTOs, el paquete debe coincidir exactamente con el nombre usado en `@Query`.

---

## ✅ Conclusión

El uso de JPQL avanzado permite optimizar la consulta de datos en Spring Boot:

* `Object[]`: flexible pero menos legible.
* Constructores de entidad: útiles para evitar sobrecarga.
* DTOs: recomendados para capas de presentación y transferencia de datos.

Estas técnicas mejoran el rendimiento al evitar cargar datos innecesarios y proporcionan una separación clara entre entidades y objetos de vista.