# 29. Asociación @OneToOne Bidireccional en JPA

La asociación `@OneToOne` bidireccional permite que ambas entidades se conozcan mutuamente y mantengan la relación sincronizada desde ambos lados.

---

## 📚 Estructura del modelo

### Entidad ClientDetails (lado inverso, con FK)

```java
@Entity
@Table(name = "clients_details")
public class ClientDetails {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private boolean premium;
    private Integer points;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    // getters y setters...
}
```

### Entidad Client (lado propietario inverso)

```java
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastname;

    // Otras relaciones omitidas por brevedad

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "client")
    private ClientDetails clientDetails;

    public void setClientDetails(ClientDetails clientDetails) {
        this.clientDetails = clientDetails;
        if (clientDetails != null) {
            clientDetails.setClient(this);
        }
    }

    public void removeClientDetails() {
        if (this.clientDetails != null) {
            this.clientDetails.setClient(null);
            this.clientDetails = null;
        }
    }

    // Getters, setters y otros métodos
}
```

---

## 🔧 Persistencia y sincronización

### Guardar una relación bidireccional

```java
Client client = new Client("Alex", "Pereira");
ClientDetails clientDetails = new ClientDetails(true, 5000);

client.setClientDetails(clientDetails); // Sincroniza ambos lados

clientRepository.save(client); // Se guarda todo en cascada
```

### Actualizar relación desde búsqueda

```java
Optional<Client> clientOp = clientRepository.findOne(1L);

clientOp.ifPresent(client -> {
    ClientDetails newDetails = new ClientDetails(true, 5000);
    client.setClientDetails(newDetails);
    clientRepository.save(client);
});
```

---

## 🧩 Explicación clave

| Concepto                    | Descripción                                                                     |
| --------------------------- | ------------------------------------------------------------------------------- |
| `mappedBy`                  | Indica que la relación es manejada por el lado `client` en `ClientDetails`      |
| `cascade = CascadeType.ALL` | Para que operaciones sobre `Client` se propaguen a `ClientDetails`              |
| `orphanRemoval = true`      | Para eliminar detalles huérfanos cuando se quita la referencia                  |
| Sincronización explícita    | El método `setClientDetails` sincroniza ambos lados para evitar inconsistencias |
| FK en `clients_details`     | La tabla `clients_details` tiene la clave foránea hacia `clients`               |

---

## 🧠 Casos de uso típicos

* Cliente y detalles exclusivos que siempre van juntos.
* Usuario y perfil detallado con información extendida.
* Persona y pasaporte (uno por uno).

---

## 🔍 Consultas y SQL generado

Hibernate realiza `INSERT` o `UPDATE` en ambas tablas y mantiene la relación por la clave foránea en `clients_details.client_id`.

---

## ✅ Buenas prácticas

* Siempre sincronizar ambas entidades para mantener la integridad.
* Usar `cascade` y `orphanRemoval` para facilitar manejo.
* Evitar ciclos infinitos en serialización si usas JSON (usar anotaciones como `@JsonManagedReference` y `@JsonBackReference` en proyectos REST).

---

## 📦 Resultado esperado

```text
Guardado: {id=1, name=John, lastname=Doe, addresses=[], invoices=[], clientDetails={id=1, premium=true, points=5000}}
```
