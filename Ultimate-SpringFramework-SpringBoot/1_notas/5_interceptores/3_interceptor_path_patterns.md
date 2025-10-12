# 3. Filtrado de Rutas con `addPathPatterns()` y `excludePathPatterns()`

Spring Boot permite aplicar interceptores **solo a rutas específicas** o excluir ciertas rutas mediante los métodos `addPathPatterns()` y `excludePathPatterns()` al registrarlos.

---

## 🎯 ¿Por qué filtrar rutas?

No siempre se desea que un interceptor actúe sobre todas las peticiones. A veces:
- Solo ciertas rutas deben ser auditadas.
- Algunas deben excluirse (como `/login`, `/health`, `/static`).

---

## 🛠️ Configuración de ejemplo

```java
@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    @Qualifier("loadingTimeInterceptor")
    private HandlerInterceptor timeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Aplica el interceptor a todas las rutas EXCEPTO /app/foo
        registry.addInterceptor(timeInterceptor)
                .excludePathPatterns("/app/foo");
                
        // También se puede aplicar solo a una ruta específica:
        // registry.addInterceptor(timeInterceptor).addPathPatterns("/app/bar");

        // O usar comodines:
        // registry.addInterceptor(timeInterceptor).addPathPatterns("/app/**");
    }
}
```

---

## 🧪 Comportamiento esperado

- Una petición a `/app/foo` **no activará** el interceptor.
- Una petición a `/app/bar` o `/app/baz` **sí lo hará**.

---

## 🔄 Combinaciones útiles

| Método                       | Descripción                                    |
|-----------------------------|------------------------------------------------|
| `.addPathPatterns("/api/**")`     | Intercepta todas las rutas bajo `/api/`       |
| `.excludePathPatterns("/login")`  | Excluye la ruta `/login` del interceptor      |
| `.addPathPatterns("/**")`         | Intercepta todas las rutas                    |
| `.addPathPatterns("/admin/**").excludePathPatterns("/admin/login")` | Combinación de ambos métodos |

---

## ✅ Conclusión

Usar `addPathPatterns()` y `excludePathPatterns()` permite aplicar interceptores de forma selectiva, optimizando el rendimiento y la lógica de tu aplicación. Es una herramienta poderosa para controlar en qué endpoints aplicar lógica transversal.
