# 2. Uso de `@Autowired` en Spring Boot

Spring Boot permite la **inyección de dependencias** de forma automática gracias a la anotación `@Autowired`. Esta puede usarse en **interfaces, setters o constructores** para inyectar beans definidos en el contenedor de Spring.

---

## 2.1 Inyección mediante Atributo (Interface + `@Autowired`)

Cuando se usa `@Autowired` directamente sobre un campo, Spring se encarga de buscar el bean correspondiente y asignarlo automáticamente. Este enfoque es simple, pero menos flexible para pruebas.

```java
@Service("serviceFoo")
public class ProductServiceFoo implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Lógica de negocio aquí
}
```

---

## 2.2 Inyección mediante Setter

Spring Boot permite la inyección a través de métodos `set`. Este método da más control si necesitas lógica adicional durante la asignación.

```java
@Service
public class ProductServiceImp implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Métodos del servicio...
}
```

---

## 2.3 Inyección mediante Constructor (sin necesidad de `@Autowired`)

Desde Spring 4.3, si una clase tiene un único constructor, Spring lo usará automáticamente para la inyección, incluso si no tiene la anotación `@Autowired`.

```java
@RestController
@RequestMapping("/api")
public class SomeController {

    private final ProductService service;

    public SomeController(@Qualifier("productServiceImp") ProductService service) {
        this.service = service;
    }

    // Controlador manejando endpoints...
}
```

> ✅ **Ventaja:** Este enfoque es el más recomendado por ser **inmutable**, **más testeable** y compatible con la inyección obligatoria.

---

## ✅ Conclusión

Spring proporciona múltiples formas de realizar inyección de dependencias usando `@Autowired`. Aunque todos los métodos son válidos, se recomienda **usar inyección por constructor** para obtener una mejor mantenibilidad, pruebas más fáciles y claridad en las dependencias requeridas.
