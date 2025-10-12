# 27. Relaciones en JPA: ManyToOne, OneToMany y OneToMany Bidireccional

En JPA, las relaciones entre entidades reflejan cómo se asocian las tablas en una base de datos relacional. Las más utilizadas son `@ManyToOne`, `@OneToMany` y su variante bidireccional. A continuación se resume su funcionamiento, características, y casos de uso típicos.

---

## 🔁 1. @ManyToOne

### ✅ ¿Qué es?
- Define una relación en la que **muchas entidades hijas se relacionan con una sola entidad padre**.
- Es **el lado propietario** de la relación (es quien tiene la `@JoinColumn`).
- Usualmente se define en la entidad "muchos".

### 📌 Ejemplo:
```java
@ManyToOne
@JoinColumn(name = "client_id")
private Client client;
```

### 🧠 Uso típico:

* Muchas facturas (`Invoice`) pertenecen a un cliente (`Client`).
* Muchos productos (`Product`) pueden pertenecer a una misma categoría (`Category`).

### ⚠️ Consideraciones:

* Se debe usar junto con `@JoinColumn` para definir la clave foránea explícitamente.
* Este lado controla el `INSERT` o `UPDATE` de la relación.

---

## 🔁 2. @OneToMany (Unidireccional)

### ✅ ¿Qué es?

* Relación inversa a `@ManyToOne`: **una entidad padre tiene muchas entidades hijas**.
* Se define usualmente como una lista o conjunto.

### 📌 Ejemplo:

```java
@OneToMany
@JoinColumn(name = "client_id") // clave foránea en tabla hija
private List<Invoice> invoices;
```

### 🧠 Uso típico:

* Un cliente (`Client`) tiene muchas facturas (`Invoice`).
* Una categoría (`Category`) contiene muchos productos (`Product`).

### ⚠️ Consideraciones:

* Si se define sin `mappedBy`, se genera una **tabla intermedia**, lo cual es costoso e innecesario en muchos casos.
* No es el dueño de la relación por defecto, salvo se indique `@JoinColumn`.

---

## 🔁 3. @OneToMany Bidireccional

### ✅ ¿Qué es?

* La **forma recomendada** cuando se necesita mantener sincronizada la relación entre padre e hijo.
* Involucra tener `@ManyToOne` en la clase hija y `@OneToMany(mappedBy = "...")` en la clase padre.

### 📌 Ejemplo:

```java
// Clase padre
@OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<Invoice> invoices;

// Clase hija
@ManyToOne
@JoinColumn(name = "client_id")
private Client client;
```

### 🧠 Uso típico:

* Permite recorrer en ambas direcciones: desde cliente a facturas y desde factura a cliente.
* Facilita operaciones como eliminar una factura de la lista del cliente.

### ⚠️ Consideraciones:

* Es necesario mantener sincronizados ambos lados con métodos como `addFactura()` y `removeFactura()`.
* Se recomienda usar `Set<>` para evitar duplicados y facilitar las operaciones de eliminación.

---

## 🧰 Casos de uso comunes

| Relación                   | Caso de uso        | Descripción                                                           |
| -------------------------- | ------------------ | --------------------------------------------------------------------- |
| `@ManyToOne`               | Factura - Cliente  | Muchas facturas para un cliente                                       |
| `@OneToMany`               | Autor - Libros     | Un autor con varios libros (si no se necesita navegar desde el libro) |
| `@OneToMany Bidireccional` | Cliente - Facturas | Para mantener sincronización completa y eliminación eficiente         |

---

## ✅ Buenas prácticas

* Siempre que se requiera control bidireccional y eliminación, usar `mappedBy` + `orphanRemoval = true`.
* Evitar colecciones como `List` si no se necesita orden, `Set` es más eficiente con `equals/hashCode`.
* Documentar claramente el lado propietario de la relación.
* Usar métodos auxiliares (`addX`, `removeX`) para mantener consistencia en relaciones bidireccionales.

---

## 🧠 Conceptos clave

* **Propietario de la relación**: quien tiene la clave foránea (`@JoinColumn`) y controla las actualizaciones.
* **`mappedBy`**: indica que ese lado no es el propietario, sino que la relación se gestiona desde el otro lado.
* **`orphanRemoval`**: permite que al eliminar un elemento de la colección, se elimine también de la base de datos.
* **`cascade`**: automatiza persistencia o eliminación de elementos hijos cuando se opera sobre el padre.
