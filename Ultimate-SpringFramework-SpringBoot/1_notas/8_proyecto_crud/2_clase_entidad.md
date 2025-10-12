### **2. Clase Entidad: `Product`**

La clase `Product` representa una entidad que se mapea a una tabla de la base de datos llamada `products`. Esta clase es fundamental para el modelo de datos del proyecto, y su propósito es gestionar la información de los productos que se almacenarán en la base de datos.

#### **2.1. Anotaciones de JPA:**

Las anotaciones de JPA (Java Persistence API) son esenciales para mapear la clase `Product` a una tabla en la base de datos.

1. **`@Entity`**: Marca la clase como una entidad JPA. Esto indica que la clase será mapeada a una tabla en la base de datos.

2. **`@Table(name = "products")`**: Define el nombre de la tabla en la base de datos. En este caso, la entidad `Product` se mapea a la tabla `products`.

3. **`@Id`**: Indica que el campo `id` es la clave primaria de la entidad, la cual se utilizará para identificar de manera única cada registro de producto en la base de datos.

4. **`@GeneratedValue(strategy = GenerationType.IDENTITY)`**: Especifica que el valor de `id` se generará automáticamente en la base de datos usando la estrategia `IDENTITY`. Esto permite que la base de datos gestione la asignación de un identificador único para cada producto.

#### **2.2. Atributos de la Entidad `Product`:**

La clase `Product` tiene los siguientes atributos, que corresponden a las columnas de la tabla `products` en la base de datos:

1. **`private Long id;`**

   * **Descripción**: Representa el identificador único del producto. Este campo se genera automáticamente por la base de datos cuando se inserta un nuevo producto.
   * **Tipo en la base de datos**: `BIGINT`.

2. **`private String name;`**

   * **Descripción**: El nombre del producto.
   * **Validación**: Se utiliza la anotación `@NotBlank` para asegurarse de que el nombre no esté vacío. También se valida la longitud con la anotación `@Size(min = 3, max = 45)` para asegurar que el nombre tenga al menos 3 caracteres y no más de 45.
   * **Tipo en la base de datos**: `VARCHAR(45)`.

3. **`private double price;`**

   * **Descripción**: El precio del producto.
   * **Validación**: Se utiliza la anotación `@NotNull` para asegurar que el precio no sea nulo y `@Min(0)` para garantizar que el precio no sea negativo.
   * **Tipo en la base de datos**: `DECIMAL`.

4. **`private String description;`**

   * **Descripción**: Una descripción del producto.
   * **Validación**: La anotación `@NotBlank` asegura que la descripción no esté vacía.
   * **Tipo en la base de datos**: `TEXT`.

#### **2.3. Métodos de la Entidad:**

Los métodos de la clase `Product` permiten obtener y establecer los valores de los atributos. Estos métodos son fundamentales para trabajar con los productos en el código y también para manipular los datos de la base de datos a través de JPA.

1. **`getId()` y `setId(Long id)`**: Métodos para obtener y establecer el identificador del producto.
2. **`getName()` y `setName(String name)`**: Métodos para obtener y establecer el nombre del producto.
3. **`getPrice()` y `setPrice(double price)`**: Métodos para obtener y establecer el precio del producto.
4. **`getDescription()` y `setDescription(String description)`**: Métodos para obtener y establecer la descripción del producto.

#### **2.4. Resumen:**

La clase `Product` es una entidad JPA que se mapea a la tabla `products` de la base de datos. La clase contiene atributos como `id`, `name`, `price`, y `description`, cada uno con su respectiva validación. Los métodos getters y setters permiten acceder y modificar estos atributos.

La entidad `Product` es un componente clave en el proyecto CRUD, ya que interactúa directamente con la base de datos para realizar las operaciones de creación, lectura, actualización y eliminación de productos.
