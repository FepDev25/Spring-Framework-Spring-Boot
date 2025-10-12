# 23. Eliminación de Hijos en `@OneToMany` con `orphanRemoval=true`

En una relación `@OneToMany`, es posible eliminar registros hijos de forma automática si se utiliza el atributo `orphanRemoval=true`. Esto garantiza que, al remover una entidad hija de la colección del padre y guardar nuevamente, dicha entidad será **eliminada físicamente** de la base de datos.

---

## 🧾 Entidades

### ✅ Clase `Client`

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
    private List<Address> addresses = new ArrayList<>();

    public Client() {}

    public Client(String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
    }

    public void addAddress(Address... addresses) {
        this.addresses.addAll(Arrays.asList(addresses));
    }

    public List<Address> getAddresses() {
        return addresses;
    }
}
```

---

### ✅ Clase `Address`

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

    // Getters, setters, equals y hashCode (importante para List.remove)
}
```

> ⚠️ Importante: Se sobrescriben los métodos `equals` y `hashCode` en `Address` para que `List.remove()` funcione correctamente y detecte la instancia a eliminar según el `id`.

---

## 🧪 Ejecución de Eliminación de Hijos

```java
@Transactional
public void removeAddress() {
    Client client1 = new Client("Fran", "Moras");

    Address address1 = new Address("Calle 1", 78);
    Address address2 = new Address("Calle 2", 79);
    Address address3 = new Address("Calle 3", 80);

    client1.addAddress(address1, address2, address3);

    Client clienteDb = clientRepository.save(client1);
    System.out.println("Guardado 1: " + clienteDb);
    
    Optional<Client> clientOp = clientRepository.findById(clienteDb.getId());

    clientOp.ifPresent(client -> {
        client.getAddresses().remove(address1);
        clientRepository.save(client);
        System.out.println("Guardado 2: " + client);
    });
}
```

---

## 🧾 Operaciones SQL Ejecutadas

```sql
-- Inserción de Cliente y Direcciones
insert into clients (lastname, name) values (?, ?);
insert into addresses (number, street) values (?, ?); -- 3 veces
insert into tbl_clientes_to_direcciones (id_cliente, id_direccion) values (?, ?); -- 3 veces

-- Eliminación de una dirección
delete from tbl_clientes_to_direcciones where id_cliente=?;
insert into tbl_clientes_to_direcciones (...) -- 2 veces (actualiza tabla intermedia)
delete from addresses where id=?; -- Dirección eliminada
```

---

## 🧠 Consideraciones

| Detalle                     | Explicación                                                                 |
| --------------------------- | --------------------------------------------------------------------------- |
| `orphanRemoval = true`      | Indica que si un hijo se elimina de la colección, también se borra de la BD |
| `cascade = CascadeType.ALL` | Permite operaciones automáticas de persistencia, eliminación, etc.          |
| `List.remove(...)`          | Usa `equals()` para encontrar el objeto a eliminar                          |
| `@JoinTable(...)`           | Se usa en lugar de `@JoinColumn` para manejar la tabla intermedia           |

---

## ✅ Resultado

```text
Guardado 1: Client con 3 direcciones
Guardado 2: Client con 2 direcciones (una eliminada físicamente)
```

---

## 📌 Conclusión

La combinación de `orphanRemoval=true` y `cascade=ALL` permite gestionar completamente la vida de los objetos hijos desde el padre. Es una forma segura de mantener la consistencia entre el estado de la aplicación y la base de datos, sin necesidad de eliminar explícitamente desde el `Repository`.
