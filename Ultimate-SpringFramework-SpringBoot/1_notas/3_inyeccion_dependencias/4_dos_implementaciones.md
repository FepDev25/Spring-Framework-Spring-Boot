# 4. Dos o Más Implementaciones de la Misma Interfaz

Cuando existen múltiples implementaciones de una misma interfaz en Spring, se pueden utilizar anotaciones como `@Primary` y `@Qualifier` para resolver ambigüedades y controlar cuál implementación se debe inyectar.

---

## 4.1 `@Primary`

- Se usa para marcar una implementación como la **predeterminada** cuando hay múltiples opciones.
- Spring la usará si no se especifica un `@Qualifier`.

```java
@Repository
@Primary
public class ProductRepositoryImp implements ProductRepository {
    // Implementación principal
}
```

---

## 4.2 `@Qualifier`

- Se usa para **especificar explícitamente** qué implementación se debe inyectar.
- Se puede aplicar sobre atributos, setters o constructores.

### 4.2.1 Cambiar el nombre lógico del componente

```java
@Service("serviceFoo")
public class ProductServiceFoo implements ProductService {
    // Esta implementación ahora tiene el nombre lógico "serviceFoo"
}
```

---

## 4.2.2 Ejemplos de uso de `@Qualifier`

### Inyección por interfaz

```java
@Autowired
@Qualifier("serviceFoo")
private ProductService service;
```

### Inyección por setter

```java
@Autowired
public void setService(@Qualifier("serviceFoo") ProductService service) {
    this.service = service;
}
```

### Inyección por constructor

```java
public SomeController(@Qualifier("productServiceImp") ProductService service) {
    this.service = service;
}
```

---

## 4.3 ¿Qué pesa más: `@Primary` o `@Qualifier`?

✅ `@Qualifier` tiene **mayor prioridad** que `@Primary`.

Si una clase está marcada como `@Primary`, pero en la inyección se usa un `@Qualifier`, **Spring respetará el `@Qualifier`**.

### Ejemplo:

```java
@Repository
@Primary
public class ProductRepositoryImp implements ProductRepository {
    // Esta es la implementación por defecto
}
```

```java
@Repository("repositoryFoo")
public class ProductRepositoryFoo implements ProductRepository {
    // Otra implementación
}
```

```java
@Autowired
@Qualifier("repositoryFoo")
private ProductRepository repo; // Spring usará esta, ignorando la marcada como @Primary
```

---

## ✅ Conclusión

En casos donde una interfaz tiene múltiples implementaciones:
- Usa `@Primary` para definir una por defecto.
- Usa `@Qualifier` para indicar específicamente cuál deseas inyectar.
- Spring siempre priorizará `@Qualifier` si se proporciona, brindando un control fino sobre las dependencias inyectadas.
