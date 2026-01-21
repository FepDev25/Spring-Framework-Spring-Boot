# 2. Entidad `Product` y su DTO asociado

En esta sección se presenta la definición de la entidad `Product`, que forma parte del modelo de dominio utilizado en la aplicación. Esta clase está mapeada a la tabla `products` de la base de datos y es gestionada por JPA (Jakarta Persistence API).

---

## Entidad: `Product`

```java
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private String description;
    private String sku;

    // Getters y Setters omitidos para brevedad
}
```

### Anotaciones Clave

| Anotación                   | Propósito                                                                      |
| --------------------------- | ------------------------------------------------------------------------------ |
| `@Entity`                   | Declara la clase como una entidad persistente.                                 |
| `@Table(name = "products")` | Asocia esta entidad a la tabla `products` en la base de datos.                 |
| `@Id`                       | Define la clave primaria de la entidad.                                        |
| `@GeneratedValue(...)`      | Genera automáticamente el valor del `id` (en este caso, tipo autoincremental). |

### Atributos Principales

* `id`: Identificador único del producto.
* `name`: Nombre del producto.
* `price`: Precio en formato decimal.
* `description`: Texto descriptivo del producto.
* `sku`: Código único de referencia (Stock Keeping Unit).

> Esta entidad representa una estructura básica para operaciones CRUD sobre productos.

---

## DTO: `ProductDTO`

El `DTO` (Data Transfer Object) se utiliza para intercambiar información entre la capa de presentación (API) y la lógica de negocio, desacoplando la entidad interna de la exposición directa.

```java
public class ProductDTO {

    private String name;
    private Double price;
    private String description;
    private String sku;

    // Getters y Setters
}
```

### ¿Por qué usar un DTO?

* Para proteger la estructura interna de la base de datos.
* Para aplicar reglas de validación (que se verán más adelante).
* Para mejorar el control sobre los datos que entran o salen de la API.

> El DTO es especialmente útil cuando se necesita diferenciar entre el modelo de dominio y los datos que se exponen o reciben vía REST.

---

## Conclusión

La entidad `Product` representa un recurso persistente en la aplicación, con mapeo completo a la tabla `products`. Su correspondiente DTO permite manejar los datos de entrada y salida de forma controlada, lo que facilita la validación y evita exponer directamente las entidades del dominio.

En próximas secciones se explicará cómo agregar validaciones a este DTO y cómo conectarlo con los controladores y servicios.
