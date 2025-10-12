# 26. Eliminación de elementos en relación `@OneToMany` Bidireccional en JPA

En esta sección se muestra cómo eliminar elementos de una relación bidireccional `@OneToMany` usando JPA y Hibernate, aprovechando la configuración `orphanRemoval = true`.

---

## 🧾 Entidades involucradas

### `Client.java`

```java
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastname;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "client")
    private Set<Invoice> invoices = new HashSet<>();

    // Otros atributos como direcciones omitidos para brevedad

    public Client() {}

    public Client(String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
    }

    public Client addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setClient(this);
        return this;
    }

    public Client removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setClient(null); // Fundamental para romper la relación bidireccional
        return this;
    }

    // Getters, setters, toString()
}
```

### `Invoice.java`

```java
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Long total;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Invoice() {}

    public Invoice(String description, Long total) {
        this.description = description;
        this.total = total;
    }

    // Getters, setters, equals, hashCode, toString()
}
```

---

## 🧪 Lógica de inserción y eliminación

```java
@Transactional
public void oneToManyRemoveBidirectionalFindById() {
    Optional<Client> clientOp = clientRepository.findOne(1L);

    if (clientOp.isPresent()) {
        Client client = clientOp.get();

        Invoice invoice1 = new Invoice("Factura No. 1", 1000L);
        Invoice invoice2 = new Invoice("Factura No. 2", 2000L);
        Invoice invoice3 = new Invoice("Factura No. 3", 3000L);

        client.addInvoice(invoice1).addInvoice(invoice2).addInvoice(invoice3);
        Client clientDb = clientRepository.save(client);
        System.out.println("Guardado 1: " + clientDb);
    }

    Optional<Client> clientOpDB = clientRepository.findOne(1L);
    clientOpDB.ifPresent(client -> {
        Optional<Invoice> invoiceOp = invoiceRepository.findById(2L);

        invoiceOp.ifPresent(invoice -> {
            client.removeInvoice(invoice);
            clientRepository.save(client); // Esto gatilla el DELETE gracias a orphanRemoval
            System.out.println("Guardado 2: " + client);
        });
    });
}
```

---

## 🔍 Explicación clave

| Elemento                       | Significado                                                                                                                         |
| ------------------------------ | ----------------------------------------------------------------------------------------------------------------------------------- |
| `orphanRemoval = true`         | Cuando se elimina una entidad de la colección y se rompe la relación inversa, Hibernate la elimina físicamente de la base de datos. |
| `invoice.setClient(null)`      | Requisito fundamental para que Hibernate detecte que la entidad está "huérfana".                                                    |
| `clientRepository.save()`      | Se requiere llamar explícitamente al repositorio para aplicar el cambio.                                                            |
| `@OneToMany(mappedBy = "...")` | Define el lado inverso, no dueño de la relación.                                                                                    |

---

## 🧾 SQL generado por Hibernate

```sql
insert into invoices (client_id, description, total) values (?, ?, ?)
-- 3 veces para las 3 facturas

delete from invoices where id = ?
-- Se elimina la factura con id=2
```

---

## ✅ Resultado esperado

```text
Guardado 1: {id=1, name=John, lastname=Doe, addresses=[], invoices=[Factura 1, 2, 3]}
Guardado 2: {id=1, name=John, lastname=Doe, addresses=[], invoices=[Factura 1, 3]}
```

---

## 🧠 Buenas prácticas

* Siempre sincronizar ambos lados de la relación (`setClient(null)`).
* Usar `Set` para evitar duplicados y asegurar eliminación correcta con `equals()` y `hashCode()`.
* Verificar que la entidad removida esté contenida en la colección antes de eliminar.
* Aplicar `orphanRemoval = true` para garantizar la eliminación automática desde la base de datos.