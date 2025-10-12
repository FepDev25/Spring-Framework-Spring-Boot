# Pasar Parámetros a Endpoints en Spring Boot

Spring Boot permite recibir parámetros en controladores REST usando diferentes enfoques según el tipo de solicitud, el origen del dato y la conveniencia del caso de uso. En este documento se presentan ejemplos prácticos con `@RequestParam`, `@PathVariable`, `HttpServletRequest` y otras formas como `@RequestBody` y `@Value`.

---

## 1. `@RequestParam`: Parámetros desde la URL (Query Parameters)

Esta anotación se usa cuando queremos obtener parámetros como `?key=value` en la URL.

### Ejemplos:

```java
@GetMapping("/foo")
public ParamDto foo (@RequestParam String message)
```

- URL de prueba: `GET /api/params/foo?message=Hola`
- Obtiene `message` desde la URL.

---

```java
@GetMapping("/foo-required")
public ParamDto fooRequired (@RequestParam(required = false, defaultValue="Default message") String message)
```

- Si no se proporciona `message`, se usará `"Default message"`.

---

```java
@GetMapping("/foo-multiple")
public ParamDtoMix fooMultiple(@RequestParam String text, @RequestParam Integer code)
```

- URL de prueba: `GET /api/params/foo-multiple?text=Hola&code=123`

---

## 2. `HttpServletRequest`: Acceso manual a parámetros

Utilizado para obtener parámetros directamente desde el objeto `HttpServletRequest`.

```java
@GetMapping("/request")
public ParamDtoMix var(HttpServletRequest request)
```

- Se accede mediante: `request.getParameter("message")`
- Requiere conversión manual de tipos, ejemplo:

```java
Integer code = Integer.valueOf(request.getParameter("code"));
```

---

## 3. `@PathVariable`: Parámetros dentro del path (URL Path Parameters)

Se usa cuando los datos vienen incrustados directamente en la URL, como parte del endpoint.

### Ejemplos:

```java
@GetMapping("/baz/{message}")
public ParamDto baz(@PathVariable String message)
```

- URL: `GET /api/path/baz/Hola`

---

```java
@GetMapping("/baz-multiple/{product}/{id}")
public Map<String, Object> bazMultiple(@PathVariable String product, @PathVariable Long id)
```

- URL: `GET /api/path/baz-multiple/teclado/123`

---

## 4. `@RequestBody`: Parámetros en el cuerpo de la solicitud (JSON)

Se usa comúnmente en peticiones `POST` donde los datos vienen en formato JSON.

### Ejemplo:

```java
@PostMapping("/create")
public User create(@RequestBody User user)
```

- Se envía un JSON como cuerpo:

```json
{
  "name": "Juan",
  "lastname": "Perez",
  "email": "juan@mail.com"
}
```

- Transforma automáticamente el JSON en un objeto `User`.

---

## 5. `@Value`: Inyección de valores desde `application.properties`

Permite extraer parámetros definidos en el archivo de configuración `application.properties`.

### Ejemplo:

```java
@Value("${config.username}")
private String username;
```

- También es posible inyectar listas o mapas:

```java
@Value("#{'${config.listOfValues}'.split(',')}")
private List<String> listOfValues2;

@Value("#{${config.valuesMap}}")
private Map<String, Object> valuesMap;
```

---

## 6. `Environment`: Acceso dinámico a variables de entorno y propiedades

Alternativa a `@Value`, útil cuando se desea obtener valores dinámicamente o aplicar transformaciones.

### Ejemplo:

```java
@Autowired
private Environment env;

@GetMapping("/environment")
public Map<String, Object> environment() {
    env.getProperty("config.code", Integer.class);
}
```

---

## Conclusión

| Tipo de parámetro     | Anotación / Objeto     | Ideal para                                  |
|------------------------|------------------------|----------------------------------------------|
| Query Params           | `@RequestParam`        | Filtros, búsquedas, flags opcionales         |
| Path Params            | `@PathVariable`        | Identificadores o recursos concretos         |
| Body (JSON)            | `@RequestBody`         | Envío de objetos completos (POST/PUT)        |
| Parámetros manuales    | `HttpServletRequest`   | Casos especiales, lógica personalizada       |
| Configuración externa  | `@Value`, `Environment`| Variables definidas en `application.properties` |

---

> Estos mecanismos hacen que Spring Boot sea muy flexible para desarrollar APIs RESTful limpias, seguras y adaptables a distintas necesidades.
