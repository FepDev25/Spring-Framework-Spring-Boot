# 6. Registrar Componentes en Otra Clase (`@Configuration` + `@Bean`)

En Spring Boot, es posible registrar componentes manualmente usando una clase anotada con `@Configuration` y métodos `@Bean`. Esto resulta útil cuando necesitas controlar el ciclo de vida o la creación de objetos de forma explícita, especialmente si no puedes usar `@Component`, `@Repository`, etc.

---

## 📦 Ejemplo de Registro Manual de un Bean

```java
@Configuration
@PropertySource(value = "classpath:values.properties", encoding = "UTF-8")
public class AppConfiguration {

    @Bean
    @Primary
    @SuppressWarnings("unused")
    ProductRepository productRepositoryJson() {
        return new ProductRepositoryJson();
    }
}
```

### Explicación:

- `@Configuration`: indica que esta clase define beans manualmente.
- `@Bean`: registra manualmente un componente dentro del contenedor Spring.
- `@Primary`: define esta implementación como predeterminada si existen múltiples opciones para una interfaz.
- `@PropertySource`: permite cargar propiedades desde un archivo `.properties` externo.

---

## 🔧 Clase `ProductRepositoryJson` sin anotaciones Spring

Este tipo de clase no se anota con `@Repository`, pero puede ser registrada manualmente.

```java
public class ProductRepositoryJson implements ProductRepository {

    private List<Product> list;

    public ProductRepositoryJson() {
        ClassPathResource resource = new ClassPathResource("product.json");
        ObjectMapper mapper = new ObjectMapper();
        try {
            list = Arrays.asList(
                mapper.readValue(resource.getFile(), Product[].class)
            );
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Product> findAll() {
        return list;
    }

    @Override
    public Product findById(Long id) {
        return list.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }
}
```

> 💡 Aunque esta clase no está anotada como componente, al estar registrada con `@Bean` en una clase `@Configuration`, Spring la reconoce como un bean válido.

---

## ✅ Conclusión

Registrar componentes con `@Bean` y `@Configuration` ofrece:
- Control explícito sobre la instancia que se inyecta.
- Capacidad de integrar clases externas o no anotadas con Spring.
- Posibilidad de definir lógica de construcción personalizada o cargar recursos como archivos JSON.

Este enfoque es útil especialmente cuando no puedes modificar la clase original o cuando necesitas lógica de inicialización compleja.





