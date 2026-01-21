# 1. Programación Orientada a Aspectos (AOP) en Spring Boot

La Programación Orientada a Aspectos (AOP) permite modularizar funcionalidades transversales que afectan a múltiples partes de una aplicación, como la autenticación, el logging, la gestión de errores o las transacciones.

---

## ¿Por qué usar AOP?

- Hace el código más **reutilizable**, **modular** y **desacoplado**.
- Permite separar la lógica de negocio principal de tareas repetitivas o transversales.
- Puede aplicarse sobre métodos de controladores, servicios, repositorios, DAO, etc.
- Ejecuta lógica **antes**, **después** o **alrededor** de un método objetivo.

---

## Conceptos Clave

### Aspect

- Es una **modularización de una preocupación transversal**, como un interceptor.
- Define qué lógica transversal se aplicará y dónde.

### Join Point (Punto de Cruce)

- Representa un punto en la ejecución del programa, como una invocación de método.
- En Spring AOP, los join points son **ejecuciones de métodos**.

### Advice

- Es el **código que se ejecuta en un join point**.
- Tipos de advice:
  - `@Before`: antes de la ejecución del método.
  - `@AfterReturning`: después de que el método retorna exitosamente.
  - `@AfterThrowing`: si el método lanza una excepción.
  - `@After`: sin importar si el método terminó con éxito o error.
  - `@Around`: alrededor de la ejecución del método (antes y después).

### Pointcut

- Es una **expresión que define los join points** donde se debe aplicar un advice.
- Puede usar expresiones regulares y patrones como `execution(* com.example.servicios.*.*(..))`.

---

## ¿Cómo se relacionan?

- **Aspecto**: define uno o más **advices**.
- Cada **advice** se asocia a un **pointcut** que selecciona los **join points** donde se ejecutará.

---

## Conclusión

Spring AOP permite implementar lógica transversal sin alterar el código de negocio directamente. A través de **aspectos**, **pointcuts** y **advices**, se logra mantener el código limpio, enfocado y fácilmente testeable. Es especialmente útil para implementar funcionalidades como seguridad, logging, manejo de errores y métricas.
