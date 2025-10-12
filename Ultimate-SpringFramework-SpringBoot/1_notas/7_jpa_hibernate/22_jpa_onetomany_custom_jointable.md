# 22. Asociación `@OneToMany` con Personalización de la Tabla Intermedia (`@JoinTable`)

Cuando se usa `@OneToMany` sin una clave foránea, Hibernate crea por defecto una **tabla intermedia** con nombres generados. Sin embargo, JPA permite **personalizar completamente esta tabla** usando la anotación `@JoinTable`.

---

## 🧾 Entidad: `Client` con `@JoinTable` personalizado

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
    @JoinTable(
        name = "tbl_clientes_to_direcciones", 
        joinColumns = @JoinColumn(name = "id_cliente"), 
        inverseJoinColumns = @JoinColumn(name = "id_direccion"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_direccion"})
    )
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

## 🧩 Entidad: `Address`

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

## 🛠️ Estructura de Tablas Generadas

```sql
create table addresses (
    id bigint not null auto_increment,
    number integer,
    street varchar(255),
    primary key (id)
);

create table clients (
    id bigint not null auto_increment,
    lastname varchar(255),
    name varchar(255),
    primary key (id)
);

create table tbl_clientes_to_direcciones (
    id_cliente bigint not null,
    id_direccion bigint not null,
    constraint UKpxt062ac377641l93fn4rm6qc unique (id_direccion)
);

alter table tbl_clientes_to_direcciones 
    add constraint FKfcybvssugj6lnncb4u1ot58ou 
    foreign key (id_direccion) references addresses (id);

alter table tbl_clientes_to_direcciones 
    add constraint FK82wlguqfhlmho960q00e01tr2 
    foreign key (id_cliente) references clients (id);
```

> 📌 Se ha renombrado la tabla intermedia a `tbl_clientes_to_direcciones` y sus columnas a `id_cliente` y `id_direccion`.

---

## 🧪 Ejecución en `CommandLineRunner`

```java
@Transactional
public void oneToMany() {
    Client client = new Client("John", "Doe");

    Address a1 = new Address("Calle 7", 12);
    Address a2 = new Address("Calle 44", 11);
    Address a3 = new Address("Calle 77", 90);

    client.addAddress(a1, a2, a3);

    Client clientDb = clientRepository.save(client);
    System.out.println("Guardado: " + clientDb);
}
```

---

## ✅ Ventajas de Personalizar la Tabla Intermedia

| Característica                    | Ventaja                                                  |
| --------------------------------- | -------------------------------------------------------- |
| Nombres legibles                  | Se mejora la claridad de la base de datos                |
| Restricciones únicas              | Se puede evitar duplicidades en las relaciones           |
| Integración con sistemas externos | Mejora la interoperabilidad con herramientas de modelado |

---

## 🧠 Conclusión

Usar `@JoinTable` en `@OneToMany` permite **controlar completamente la tabla intermedia** en relaciones unidireccionales, aportando flexibilidad tanto en nombre de columnas como en restricciones. Aunque Hibernate genera una tabla automáticamente, la personalización puede ser necesaria en entornos productivos o integraciones con otras bases de datos.