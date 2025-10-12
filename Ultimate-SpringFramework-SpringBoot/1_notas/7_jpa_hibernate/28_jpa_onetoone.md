# 28. Asociación @OneToOne en JPA

La anotación `@OneToOne` se utiliza para modelar relaciones uno a uno entre dos entidades. Es decir, **una instancia de una entidad está relacionada con exactamente una instancia de otra entidad**.

---

## 🧩 Estructura básica

```java
// Entidad principal
@OneToOne
private ClientDetails clientDetails;
```

```java
// Entidad secundaria
@Entity
@Table(name = "clients_details")
public class ClientDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean premium;
    private Integer points;
}
```

---

## ⚙️ Características de @OneToOne

| Característica           | Detalles                                            |
| ------------------------ | --------------------------------------------------- |
| Dirección de la relación | Puede ser unidireccional o bidireccional            |
| Clave foránea            | Se puede definir con `@JoinColumn`                  |
| Cascade                  | Útil para guardar automáticamente ambas entidades   |
| Orphan Removal           | Permite eliminar detalles si se elimina la relación |
| Mapeo bidireccional      | Se logra con `mappedBy` desde la otra clase         |

---

## 📌 Ejemplo de uso real

### Crear y asociar un detalle a un cliente

```java
ClientDetails clientDetails = new ClientDetails(true, 5000);
clientDetailsRepository.save(clientDetails);

Client client = new Client("Alex", "Pereira");
client.setClientDetails(clientDetails);
clientRepository.save(client);
```

### Resultado esperado

Hibernate insertará en la tabla `clients_details` y luego en `clients`, asociando el `client_details_id` correctamente.

```sql
insert into clients_details (premium, points) values (?, ?)
insert into clients (name, lastname, client_details_id) values (?, ?, ?)
```

---

## 🔄 Asociación desde el otro lado (opcional)

Si se desea acceder al cliente desde `ClientDetails`, se puede hacer la relación bidireccional:

```java
@OneToOne(mappedBy = "clientDetails")
private Client client;
```

---

## 🧠 Casos de uso típicos

| Entidad Principal | Detalle          | Descripción                                     |
| ----------------- | ---------------- | ----------------------------------------------- |
| Cliente           | Detalles Cliente | Información extendida como puntos o membresía   |
| Usuario           | Perfil           | Perfil con datos adicionales o de configuración |
| Persona           | Pasaporte        | Una persona tiene un pasaporte único            |

---

## ⚠️ Buenas prácticas

* Usar `cascade = CascadeType.ALL` si se quiere guardar ambos objetos en una sola operación.
* Usar `optional = false` si la relación debe ser obligatoria.
* Considerar el uso de `orphanRemoval = true` si se desea eliminar el detalle al remover la relación.
* Controlar las relaciones bidireccionales con métodos auxiliares para mantener la sincronización.

---

## 🧪 Métodos útiles

```java
// Al buscar un cliente, se puede acceder al detalle directamente
Client client = clientRepository.findById(1L).orElseThrow();
System.out.println(client.getClientDetails());

// También se puede asignar un detalle a un cliente ya existente
client.setClientDetails(new ClientDetails(true, 1000));
clientRepository.save(client);
```

---

## 📝 Conclusión

`@OneToOne` es ideal para modelar relaciones exclusivas entre dos entidades. Aunque puede parecer sencilla, su uso correcto mejora la normalización y el diseño del modelo de datos.

