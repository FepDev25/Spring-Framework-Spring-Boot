# Uso de Spring Expression Language (SpEL) en `@Value`

Spring Expression Language (SpEL) permite evaluar expresiones complejas en el contexto de Spring, como acceder a propiedades, métodos, colecciones, e incluso hacer transformaciones.

---

## 🧩 Diferencias clave: `${...}` vs `#{${...}}`

### 1. `@Value("${...}")`
- **Uso**: Simple sustitución de propiedades desde `application.properties` o archivos `.properties` personalizados.
- **Limitación**: Solo retorna valores como `String`, `int`, `boolean`, etc.
- ❌ No puede transformar un `String` en una estructura compleja como `Map` o `List`.

```java
@Value("${config.username}")
private String username;
```

---

### 2. `@Value("#{${...}}")`
- **Uso**: Combinación de propiedad y evaluación con SpEL.
- **Ventaja**: Permite que el valor sea interpretado como una estructura (ej. `Map`, `List`, etc.).
- ✅ Útil para convertir Strings a objetos complejos.

```java
@Value("#{${config.valuesMap}}")
private Map<String, Object> valuesMap;
```

📌 **Requiere que el contenido de la propiedad tenga sintaxis válida de SpEL**, no JSON.

---

## ✅ Ejemplo funcional en `.properties`

```properties
config.valuesMap=product:'Cpu Intel Core I7 12th', descriptions:'Alder Lake, 12 core, a GHz', price:'675'
```

Esto se interpretará correctamente como un `Map<String, Object>` si se usa con:

```java
@Value("#{${config.valuesMap}}")
private Map<String, Object> valuesMap;
```

---

## ❌ Ejemplo que falla

```properties
config.valuesMap={"product":"Cpu", "price":"675"}
```

Este valor **es JSON**, y **SpEL no lo interpreta correctamente**. Para JSON, deberías leerlo como `String` y usar Jackson:

```java
@Value("${config.valuesMap}")
private String json;
```

```java
Map<String, Object> map = objectMapper.readValue(json, new TypeReference<>() {});
```

---

## 🛠 Otros usos comunes de SpEL

```java
@Value("#{2 * 2}") // 4
@Value("#{T(Math).PI}") // Usa clase Math
@Value("#{someBean.someMethod()}") // Llama a método de otro bean
@Value("#{list.size()}") // Accede a tamaño de lista
```

---

## 📌 Conclusión

| Sintaxis           | Uso principal                    | Soporta estructuras |
|--------------------|----------------------------------|---------------------|
| `${...}`           | Sustitución directa de propiedades | ❌ Solo tipos simples |
| `#{...}`           | Evaluación SpEL                    | ✅ Totalmente dinámico |
| `#{${...}}`        | Evaluación de propiedad como estructura | ✅ Mapas, listas, operaciones |

SpEL potencia el uso dinámico de configuraciones y beans dentro del contexto de Spring Boot.
