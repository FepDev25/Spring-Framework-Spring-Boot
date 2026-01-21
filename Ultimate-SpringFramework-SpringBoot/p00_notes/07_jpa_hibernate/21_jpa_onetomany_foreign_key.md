# 21. Asociación `@OneToMany` con Clave Foránea Directa (`@JoinColumn`)

Otra forma de modelar una relación `@OneToMany` en JPA es usar directamente una **clave foránea en la tabla hija**, en lugar de una tabla intermedia. Esto se logra con la anotación `@JoinColumn`.

---

## Entidad: `Client`

```java
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastname;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "client_id")  // clave foránea en "addresses"
    private List<Address> addresses;

    public Client() {
        addresses = new ArrayList<>();
    }

    public Client(String name, String lastname) {
        this();
        this.name = name;
        this.lastname = lastname;
    }

    public void addAddress(Address... addresses) {
        this.addresses.addAll(Arrays.asList(addresses));
    }
}
```

---

## Entidad: `Address`

```java
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private Integer number;

    public Address() {}

    public Address(String street, Integer number) {
        this.street = street;
        this.number = number;
    }
}
```

### Detalles importantes

* `@JoinColumn(name = "client_id")`: Define una **columna en la tabla `addresses`** que actúa como **clave foránea** hacia `clients`.
* Hibernate ya no crea una tabla intermedia (`clients_addresses`), sino que usa una columna `client_id` en `addresses`.

---

## Ejecución en `CommandLineRunner`

```java
@Transactional
public void oneToMany() {
    Client client1 = new Client("Fran", "Moras");

    Address address1 = new Address("Calle 1", 78);
    Address address2 = new Address("Calle 2", 79);
    Address address3 = new Address("Calle 3", 80);

    client1.addAddress(address1, address2, address3);

    Client clienteDb = clientRepository.save(client1);
    System.out.println("Guardado: " + clienteDb);
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

create table addresses (
    id bigint not null auto_increment,
    street varchar(255),
    number integer,
    client_id bigint,
    primary key (id),
    foreign key (client_id) references clients(id)
);
```

> Nota: Aquí `client_id` actúa como **clave foránea en la tabla hija**, lo que simplifica el modelo relacional y evita la creación de tablas intermedias.

---

## Ventajas de usar `@JoinColumn` en `@OneToMany`

| Ventaja                      | Descripción                                             |
| ---------------------------- | ------------------------------------------------------- |
| Simplicidad                  | Menos tablas y relaciones explícitas                    |
| Consulta más directa         | Acceso más rápido a datos sin unir tres tablas          |
| Integridad referencial clara | Foreign key bien definida directamente en la tabla hija |

---

## Conclusión

Este modelo `@OneToMany` con `@JoinColumn` es útil cuando se desea un diseño de base de datos más simple y **una relación unidireccional** con referencia clara desde hijo a padre. Hibernate actualiza automáticamente la clave foránea al persistir las entidades relacionadas.
