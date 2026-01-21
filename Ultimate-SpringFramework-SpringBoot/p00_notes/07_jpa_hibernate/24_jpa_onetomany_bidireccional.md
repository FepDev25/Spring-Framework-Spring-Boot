# 24. Asociación `@OneToMany` Bidireccional en JPA

En una relación `@OneToMany` bidireccional, ambos extremos (padre e hijo) se relacionan explícitamente. El hijo contiene la clave foránea mediante `@ManyToOne`, y el padre declara la colección con `mappedBy`.

---

## Entidades

### Clase `Client` (Padre)

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
    private List<Invoice> invoices = new ArrayList<>();

    public Client() {}

    public Client(String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }
}
```

---

### Clase `Invoice` (Hijo)

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

> Nota: En este caso, `mappedBy = "client"` indica que la columna `client_id` está en la entidad `Invoice`.

---

## Ejemplo de Guardado

```java
@Transactional
public void oneToManyBidirectional() {
    Client client = new Client("Cristiano", "Ronaldo");

    Invoice invoice1 = new Invoice("Factura No. 1", 1000L);
    Invoice invoice2 = new Invoice("Factura No. 2", 2000L);
    Invoice invoice3 = new Invoice("Factura No. 3", 3000L);

    List<Invoice> invoices = Arrays.asList(invoice1, invoice2, invoice3);
    client.setInvoices(invoices);

    // Establecer la relación inversa
    invoice1.setClient(client);
    invoice2.setClient(client);
    invoice3.setClient(client);    

    clientRepository.save(client);

    System.out.println("Guardado: " + client);
}
```

---

## SQL Generado

```sql
insert into clients (lastname, name) values ('Ronaldo', 'Cristiano');
insert into invoices (client_id, description, total) values (1, 'Factura No. 1', 1000);
insert into invoices (client_id, description, total) values (1, 'Factura No. 2', 2000);
insert into invoices (client_id, description, total) values (1, 'Factura No. 3', 3000);
```

---

## Consideraciones

| Concepto                      | Detalle                                                                          |
| ----------------------------- | -------------------------------------------------------------------------------- |
| `mappedBy="client"`           | Se refiere al campo en `Invoice` que contiene la relación inversa                |
| `orphanRemoval=true`          | Si se elimina una factura de la lista y se guarda el cliente, se elimina         |
| Necesidad de doble asignación | Se debe asignar tanto el cliente en cada factura como las facturas en el cliente |

---

## Resultado

```text
Guardado: 
{id=4, name=Cristiano, lastname=Ronaldo, 
 invoices=[{id=1, description=Factura No. 1, total=1000}, 
           {id=2, description=Factura No. 2, total=2000}, 
           {id=3, description=Factura No. 3, total=3000}]}
```

---

## Conclusión

La relación `@OneToMany` bidireccional permite navegar desde el cliente a sus facturas y viceversa. Se recomienda mantener sincronizados ambos lados de la relación para evitar inconsistencias en JPA/Hibernate.
