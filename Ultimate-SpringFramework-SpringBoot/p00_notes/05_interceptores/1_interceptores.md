# 1. Interceptores en Spring Boot

Los interceptores son mecanismos que permiten interceptar peticiones HTTP **antes, durante o después** de su procesamiento en los controladores. Son útiles para aplicar lógica común y transversal, como autenticación, logging, validaciones, entre otros.

---

## ¿Qué son los interceptores?

- Son fragmentos de código reutilizable que se ejecutan **antes y/o después** del método del controlador.
- Son ideales para separar lógica transversal que no debería estar acoplada al controlador.

### Usos comunes

- Autenticación y autorización.
- Registro de logs (logger).
- Medición de tiempo de ejecución.
- Gestión de transacciones.

---

## Funcionamiento de los Interceptores

Para crear un interceptor, se implementa la interfaz `HandlerInterceptor`. Esta define tres métodos clave:

### 1. `boolean preHandle(...)`

- Se ejecuta **antes** del método del controlador.
- Si devuelve `true`, se continúa con la ejecución.
- Si devuelve `false`, el flujo se interrumpe y el controlador no se ejecuta.

### 2. `void postHandle(...)`

- Se ejecuta **después** de la ejecución del controlador, pero **antes** de que la vista sea renderizada.

### 3. `void afterCompletion(...)`

- Se ejecuta **al finalizar completamente** el ciclo de la petición.
- Útil para limpieza de recursos o reportes de errores.

---

## Ejemplo de implementación: `LoadingTimeInterceptor`

```java
@Component("loadingTimeInterceptor")
public class LoadingTimeInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoadingTimeInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HandlerMethod controller = (HandlerMethod) handler;
        logger.info("LoadingTimeInterceptor: preHandle() entrando: " + controller.getMethod().getName());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
        HandlerMethod controller = (HandlerMethod) handler;
        logger.info("LoadingTimeInterceptor: postHandle() saliendo: " + controller.getMethod().getName());
    }
}
```

> En este ejemplo, se registra el nombre del método del controlador que se está ejecutando, tanto al iniciar como al finalizar.

---

## Conclusión

Los interceptores en Spring permiten aplicar lógica transversal sin contaminar los controladores. Son fáciles de implementar y poderosos para tareas comunes como logging, seguridad o métricas de rendimiento.
