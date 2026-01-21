# 20. Asociación `@OneToMany` con Tabla Intermedia (Hibernate por Defecto)

En JPA, una relación `@OneToMany` se puede mapear de dos formas:

- Directamente con `mappedBy` y clave foránea.
- Usando una **tabla intermedia**, como lo hace Hibernate por defecto cuando **no se especifica `mappedBy`**.

Este ejemplo demuestra cómo Hibernate genera dicha tabla intermedia al relacionar una entidad `Client` con una lista de `Address`.

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
    private List<Address> addresses;

    public Client() {
        addresses = new ArrayList<>();
    }

    public Client(String name, String lastname) {
        this();
        this.name = name;
        this.lastname = lastname;
    }

    public void addAddress(Address... addr) {
        addresses.addAll(Arrays.asList(addr));
    }
}
```

### Explicación

- `@OneToMany`: Establece la relación.
- **Sin `mappedBy`** → Hibernate genera una **tabla intermedia automática** `clients_addresses`.
- `cascade = ALL`: Guarda las direcciones junto al cliente.
- `orphanRemoval = true`: Elimina direcciones si son removidas del cliente.

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

## Estructura de Tablas Generada (por Hibernate)

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
    primary key (id)
);

create table clients_addresses (
    client_id bigint not null,
    addresses_id bigint not null,
    unique (addresses_id),
    foreign key (client_id) references clients(id),
    foreign key (addresses_id) references addresses(id)
);
```

> Nota: Esta tabla `clients_addresses` es generada automáticamente cuando no se define `mappedBy`.

---

## Notas Clave

| Elemento                    | Descripción                                            |
| --------------------------- | ------------------------------------------------------ |
| `@OneToMany` sin `mappedBy` | Hibernate genera tabla intermedia                      |
| `CascadeType.ALL`           | Guarda automáticamente las direcciones                 |
| `orphanRemoval = true`      | Si se elimina de la lista, también se elimina de la BD |
| `clients_addresses`         | Tabla con relaciones `client_id` ↔ `addresses_id`      |

---

## Conclusión

Este patrón de `@OneToMany` con tabla intermedia es útil cuando no se necesita navegación bidireccional inmediata. Hibernate lo implementa automáticamente cuando no se define `mappedBy`. No obstante, si se busca evitar una tabla adicional, se recomienda usar `mappedBy` y definir la relación inversa con `@ManyToOne`.
