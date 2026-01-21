# 19. Asociación `@ManyToOne` en JPA: Relación de Muchos a Uno

En JPA, la anotación `@ManyToOne` se utiliza para establecer una relación donde **muchas entidades están asociadas a una sola**. En este ejemplo, múltiples facturas (`Invoice`) están asociadas a un solo cliente (`Client`).

---

## Modelo de Entidad: `Client`

```java
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastname;

    public Client() {}

    public Client(String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
    }
}
```

---

## Modelo de Entidad: `Invoice`

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

    public void setClient(Client client) {
        this.client = client;
    }
}
```

### Explicación

* `@ManyToOne`: Indica que muchas facturas pueden estar asociadas a un mismo cliente.
* `@JoinColumn(name = "client_id")`: Define la columna en la tabla `invoices` que actuará como **clave foránea**.

---

## Persistencia en `CommandLineRunner`

```java
public void manyToOne() {
    Client client1 = new Client("Roberto", "Sanchez");
    clientRepository.save(client1);

    Invoice invoice1 = new Invoice("Factura No. 1", 1000L);
    invoice1.setClient(client1);
    Invoice invoiceDB = invoiceRepository.save(invoice1);

    System.out.println("Factura guardada: " + invoiceDB);
}
```

```java
public void manyToOneFindById() {
    Optional<Client> clientOp = clientRepository.findById(1L);

    if (clientOp.isPresent()) {
        Client client = clientOp.get();

        Invoice invoice1 = new Invoice("Factura No. 1", 1000L);
        invoice1.setClient(client);
        Invoice invoiceDB = invoiceRepository.save(invoice1);

        System.out.println("Factura guardada: " + invoiceDB);
    }
}
```

---

## Estructura de Tablas Generadas

```sql
create table clients (
    id bigint not null auto_increment,
    lastname varchar(255),
    name varchar(255),
    primary key (id)
);

create table invoices (
    id bigint not null auto_increment,
    description varchar(255),
    total bigint,
    client_id bigint,
    primary key (id),
    foreign key (client_id) references clients(id)
);
```

---

## ¿Cuándo usar `@ManyToOne`?

* Cuando muchos registros comparten una referencia común.
* Ejemplos: muchas facturas para un cliente, muchas órdenes para un usuario, muchos comentarios para una publicación.

---

## Conclusión

La relación `@ManyToOne` es fundamental en bases de datos relacionales. En JPA, su implementación con `@JoinColumn` permite definir claves foráneas fácilmente, facilitando la navegación y persistencia entre entidades relacionadas.
