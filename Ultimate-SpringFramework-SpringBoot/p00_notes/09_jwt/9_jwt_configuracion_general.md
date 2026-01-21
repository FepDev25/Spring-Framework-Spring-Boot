# 9. Clases de Configuración General

En este módulo se definen configuraciones auxiliares de la aplicación, necesarias para funcionalidades como la internacionalización de mensajes de validación y el acceso estático al `ApplicationContext` de Spring.

---

## 1. Clase `AppConfig` – Configuración de mensajes

```java
@Configuration
public class AppConfig {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages");
        source.setDefaultEncoding("UTF-8");
        return source;
    }
}
```

### Propósito

* Define un **bean `MessageSource`** que permite la carga de mensajes desde archivos `.properties`, típicamente para:

  * Mensajes de error personalizados.
  * Internacionalización (i18n).
  * Validaciones con `@NotBlank`, `@Size`, etc.

### Detalles importantes

| Configuración                 | Significado                                                                       |
| ----------------------------- | --------------------------------------------------------------------------------- |
| `setBasename("messages")`     | Busca archivos llamados `messages.properties` o `messages_es.properties`, etc.    |
| `setDefaultEncoding("UTF-8")` | Garantiza que los mensajes sean legibles con acentos, caracteres especiales, etc. |

> Recomendado usar `ValidationMessages.properties` o `messages.properties` en `resources/` para almacenar los textos.

---

## 2. Clase `SpringContext` – Acceso estático a beans

```java
@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContext.context = applicationContext;
    }

    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }
}
```

### Propósito SpringContext

Permitir el **acceso estático a beans** definidos en el contenedor de Spring desde clases donde la inyección con `@Autowired` no es posible (por ejemplo, en validadores personalizados o código externo a Spring).

### Cómo funciona

* Implementa `ApplicationContextAware`, lo que permite que Spring inyecte su contexto al iniciar la app.
* Usa un campo `static` para guardar la instancia global del contexto.
* Permite obtener cualquier bean con:

```java
MyService service = SpringContext.getBean(MyService.class);
```

> Muy útil en clases sin soporte directo de Spring, como filtros, anotaciones personalizadas, validadores, etc.

---

## Conclusión

Estas clases de configuración aportan capacidades esenciales al proyecto:

* `AppConfig` habilita mensajes personalizados e internacionalización.
* `SpringContext` permite acceder dinámicamente a beans de Spring desde cualquier parte del código.

Ambas son utilidades transversales que complementan la infraestructura base de Spring Security + JWT.
