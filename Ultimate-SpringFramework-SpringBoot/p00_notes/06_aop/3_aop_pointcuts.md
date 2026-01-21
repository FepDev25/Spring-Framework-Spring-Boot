# 3. Pointcuts en Spring AOP: Sintaxis y Expresiones

Un **pointcut** en Spring AOP es una **expresión que define qué métodos deben ser interceptados** por un `advice`. Utilizan una sintaxis especial basada en patrones que permite seleccionar métodos según nombre, visibilidad, tipo de retorno, clase, paquete, argumentos, entre otros.

---

## ¿Qué hace un Pointcut?

- Asocia un `advice` (lógica transversal) a uno o más métodos objetivo.
- Permite interceptar métodos de clases específicas o de todo un paquete.

---

## Sintaxis General de un Pointcut

```java
execution(modificadores? tipoRetorno nombreClase.nombreMetodo(parametros))
```

Donde:

- `modificadores`: opcional (e.g., `public`)
- `tipoRetorno`: tipo de retorno del método (`*` para cualquier tipo)
- `nombreClase`: nombre completo de la clase (incluye el paquete)
- `nombreMetodo`: nombre del método o `*` para cualquiera
- `parametros`: parámetros esperados o `..` para cualquier cantidad

---

## Ejemplos de expresiones `execution`

| Expresión Pointcut | Qué intercepta |
| -------------------- | ---------------- |
| `execution(* com.example.servicio.*.*(..))` | Todos los métodos de todas las clases en el paquete `servicio` |
| `execution(public * *(..))` | Todos los métodos públicos |
| `execution(* com.example.servicio.MiServicio.metodoA(..))` | Solo el método `metodoA` de `MiServicio` |
| `execution(* *.get*(..))` | Todos los métodos que comienzan con `get` en cualquier clase |
| `execution(* set*(String))` | Todos los métodos `set` que reciben un `String` como parámetro |
| `execution(String com.example..*Service.*(..))` | Todos los métodos que devuelven `String` en cualquier clase que termine con `Service` dentro del paquete `com.example` o subpaquetes |

---

## Otros ejemplos útiles

### Métodos con argumentos específicos

```java
@Before("execution(* *.guardar(String, int))")
```

Intercepta cualquier método `guardar` con parámetros `String` e `int`.

### Métodos de una interfaz

```java
@Before("execution(* com.example.servicio.MiInterfaz.*(..))")
```

Intercepta todos los métodos definidos en la interfaz `MiInterfaz`.

---

## Comodines en expresiones

| Símbolo | Significado |
| --------- | ------------- |
| `*` | Un solo elemento (nombre de clase, método o tipo de retorno) |
| `..` | Cualquier número de argumentos o subpaquetes |
| `()` | Método sin argumentos |
| `(..)` | Método con cualquier número y tipo de argumentos |

---

## Buenas prácticas

- Utiliza expresiones lo más específicas posible para evitar interceptar métodos innecesarios.
- Usa `*` y `..` con precaución en aplicaciones grandes.
- Separa las expresiones en métodos nombrados (`@Pointcut`) para mejorar la legibilidad.

---

## Ejemplo con `@Pointcut` reutilizable

```java
@Aspect
@Component
public class MiAspecto {

    @Pointcut("execution(* com.example.servicio.*.*(..))")
    public void todosLosServicios() {}

    @Before("todosLosServicios()")
    public void logBefore(JoinPoint joinPoint) {
        // Lógica de logging antes de cualquier método en servicio
    }
}
```

---

## Conclusión

Los `pointcuts` son el corazón de AOP, permitiendo aplicar lógicas transversales de forma selectiva y precisa. Dominar su sintaxis ayuda a aplicar interceptores con claridad, seguridad y alto rendimiento.
