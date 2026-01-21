# 2. Flujo de Funcionamiento de un Interceptor en Spring Boot

Para que un interceptor funcione correctamente, deben cumplirse ciertos pasos de configuración y ejecución. A continuación se detalla el flujo completo desde su definición hasta su activación al recibir una petición HTTP.

---

## Estructura del Interceptor

### Clase: `LoadingTimeInterceptor`

```java
@Component("loadingTimeInterceptor")
public class LoadingTimeInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoadingTimeInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HandlerMethod controller = (HandlerMethod) handler;
        logger.info("LoadingTimeInterceptor: preHandle() entrando: " + controller.getMethod().getName());

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
        HandlerMethod controller = (HandlerMethod) handler;

        long endTime = System.currentTimeMillis();
        long startTime = (Long) request.getAttribute("startTime");
        long executionTime = endTime - startTime;
        
        logger.info("Tiempo de ejecución: " + executionTime + " ms");
        logger.info("LoadingTimeInterceptor: postHandle() saliendo: " + controller.getMethod().getName());
    }
}
```

- **`preHandle`**: Guarda el tiempo inicial antes de que se ejecute el controlador.
- **`postHandle`**: Calcula el tiempo total de ejecución y lo registra.

---

## Registro del Interceptor

### Clase: `AppConfig`

```java
@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    @Qualifier("loadingTimeInterceptor")
    private HandlerInterceptor timeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timeInterceptor);
    }
}
```

- Se implementa `WebMvcConfigurer` para sobrescribir el método `addInterceptors`.
- Se registra el interceptor usando `InterceptorRegistry`.

> Esto asegura que Spring detecte el interceptor y lo aplique a todas las rutas por defecto.

---

## Activación del Interceptor

### Clase: `AppController`

```java
@RestController
@RequestMapping("/app")
public class AppController {

    @GetMapping("/foo")
    public Map<String, String> foo() {
        return Map.of("message", "Handler foo del controlador");
    }

    @GetMapping("/bar")
    public Map<String, String> bar() {
        return Map.of("message", "Handler bar del controlador");
    }

    @GetMapping("/baz")
    public Map<String, String> baz() {
        return Map.of("message", "Handler baz del controlador");
    }
}
```

- Cualquier petición a `/app/foo`, `/app/bar` o `/app/baz` será interceptada.
- El interceptor registra la entrada y salida, así como el tiempo de ejecución.

---

## Flujo completo de ejecución

1. Se realiza una petición HTTP a una ruta del controlador (`/app/foo`).
2. Spring ejecuta el método `preHandle()` del interceptor.
3. Si `preHandle()` retorna `true`, el método del controlador se ejecuta.
4. Después, se ejecuta `postHandle()` del interceptor.
5. La respuesta se envía al cliente.
6. (Opcional) `afterCompletion()` puede ejecutarse si se implementa.

---

## Conclusión

El funcionamiento de un interceptor requiere:

- Implementar la lógica deseada en `HandlerInterceptor`.
- Registrar el interceptor en una clase de configuración.
- Tener controladores cuyas rutas serán interceptadas.

Este flujo modulariza comportamientos comunes sin modificar directamente los controladores.
