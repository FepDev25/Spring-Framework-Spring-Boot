# 25. Relación `@OneToMany` Bidireccional con Optimización de Fetch en JPA

En esta sección se extiende la relación `@OneToMany` bidireccional para mejorar el rendimiento al recuperar entidades que tienen múltiples relaciones con otras tablas, utilizando `fetch join` en JPQL.

---

## 🧾 Entidad `Client` con múltiples asociaciones

Se tienen dos relaciones unidireccionales hacia `Address` y una relación bidireccional con `Invoice`.

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
    private Set<Invoice> invoices;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "tbl_clientes_to_direcciones",
        joinColumns = @JoinColumn(name = "id_cliente"),
        inverseJoinColumns = @JoinColumn(name = "id_direccion"),
        uniqueConstraints = @UniqueConstraint(columnNames = "id_direccion"))
    private List<Address> addresses;

    public Client() {
        addresses = new ArrayList<>();
        invoices = new HashSet<>();
    }

    public Client(String name, String lastname) {
        this();
        this.name = name;
        this.lastname = lastname;
    }

    public Client addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setClient(this);
        return this;
    }

    public void addAddress(Address... address) {
        this.addresses.addAll(Arrays.asList(address));
    }

    // getters, setters, toString()
}
```

---

## 🧾 Entidad `Invoice`

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

    // getters, setters
}
```

---

## 📦 Repositorio con `@Query` y `fetch join`

Evita el problema clásico de **`LazyInitializationException`** y mejora el rendimiento:

```java
public interface ClientRepository extends CrudRepository<Client, Long> {

    @Query("SELECT c FROM Client c " +
           "LEFT JOIN FETCH c.addresses " +
           "LEFT JOIN FETCH c.invoices " +
           "WHERE c.id = ?1")
    Optional<Client> findOne(Long id);
}
```

✅ Este `@Query` personaliza la recuperación del cliente para incluir todas sus direcciones e invoices en **una sola consulta JOIN**.

---

## 🧪 Lógica de inserción de facturas con búsqueda previa

```java
@Transactional
public void oneToManyBidirectionalFindById() {
    Optional<Client> clientOp = clientRepository.findOne(1L);

    if (clientOp.isPresent()) {
        Client client = clientOp.get();

        Invoice invoice1 = new Invoice("Factura No. 1", 1000L);
        Invoice invoice2 = new Invoice("Factura No. 2", 2000L);
        Invoice invoice3 = new Invoice("Factura No. 3", 3000L);

        client.addInvoice(invoice1)
              .addInvoice(invoice2)
              .addInvoice(invoice3);

        clientRepository.save(client);

        System.out.println("Guardado: " + client);
    }
}
```

---

## 🧠 Detalles importantes

| Elemento                          | Explicación                                                                   |
| --------------------------------- | ----------------------------------------------------------------------------- |
| `Set<Invoice>` en lugar de `List` | Evita duplicados en colecciones, útil en relaciones bidireccionales.          |
| `@JoinTable` con direcciones      | Relación unidireccional con tabla intermedia.                                 |
| `@Query` con `LEFT JOIN FETCH`    | Carga en una sola consulta los elementos relacionados evitando consultas N+1. |
| `orphanRemoval = true`            | Elimina huérfanos de la relación si se remueven del set/lista.                |
| `mappedBy="client"`               | Indica que `Invoice` es el dueño de la relación.                              |

---

## 🔍 SQL generado

```sql
select c1_0.id, a1_0.id_cliente, a1_1.id, a1_1.number, a1_1.street,
       i1_0.client_id, i1_0.id, i1_0.description, i1_0.total,
       c1_0.lastname, c1_0.name
from clients c1_0
left join tbl_clientes_to_direcciones a1_0 on c1_0.id = a1_0.id_cliente
left join addresses a1_1 on a1_1.id = a1_0.id_direccion
left join invoices i1_0 on c1_0.id = i1_0.client_id
where c1_0.id = ?
```

---

## ✅ Resultado

```text
Guardado: 
{id=1, name=John, lastname=Doe, 
 addresses=[], 
 invoices=[{id=null, description=Factura No. 2, total=2000}, 
           {id=null, description=Factura No. 3, total=3000}, 
           {id=null, description=Factura No. 1, total=1000}]}
```

---

## 🧩 Conclusión

Esta implementación muestra cómo manejar múltiples relaciones (`OneToMany`) de forma eficiente utilizando:

* Colecciones correctas (`Set` vs `List`)
* Configuración apropiada (`mappedBy`, `orphanRemoval`)
* Consultas optimizadas (`@Query` con `fetch join`) para evitar carga perezosa no controlada.

Esto es esencial en sistemas con múltiples relaciones anidadas para mantener consistencia y buen rendimiento.
