# Directivas básicas de Thymeleaf

A continuación se presentan algunas de las directivas más utilizadas en Thymeleaf:

## 1. Mostrar texto (`th:text`)

Se utiliza para mostrar texto de forma segura (escapando HTML).

```html
<p th:text="${mensaje}">Texto por defecto</p>
```
- `mensaje` es una variable del modelo.
- El contenido del párrafo será reemplazado por el valor de `mensaje`.

---

## 2. Condicional (`th:if`)

Permite mostrar o no un fragmento de HTML dependiendo de una condición.

```html
<div th:if="${usuario != null}">
  ¡Bienvenido, <span th:text="${usuario.nombre}">Nombre</span>!
</div>
```
- Solo se mostrará el `div` si `usuario` **no es nulo**.

---

## 3. Listas y Bucles (`th:each`)

Se utiliza para recorrer una lista o colección de elementos.

```html
<ul>
  <li th:each="item : ${listaDeProductos}" th:text="${item.nombre}">Producto</li>
</ul>
```
- `item` es el nombre de la variable temporal.
- `listaDeProductos` es una lista pasada en el modelo.
- Cada `li` mostrará el nombre de un producto.

---

## 4. Índice en un bucle

Puedes acceder al índice del bucle usando `Stat`.

```html
<ul>
  <li th:each="item, stat : ${listaDeProductos}">
    <span th:text="${stat.index}"></span> - <span th:text="${item.nombre}"></span>
  </li>
</ul>
```
- `stat.index` empieza en 0 (cero).
- `stat.count` empieza en 1 (uno).

---

## 5. Condicional dentro de un bucle

Puedes usar `th:if` dentro de `th:each`.

```html
<ul>
  <li th:each="producto : ${listaDeProductos}" th:if="${producto.stock > 0}">
    <span th:text="${producto.nombre}"></span> (Disponible)
  </li>
</ul>
```
- Solo se mostrarán los productos que tengan `stock` mayor a 0.

---

## 6. Enlaces dinámicos (`th:href`)

Se usa para generar URLs que incluyen **parámetros de ruta** (`path variables`) o **parámetros de consulta** (`request parameters`), de forma segura y dinámica.

### 🔹 Parámetros tipo *Request Param*

```html
<a th:href="@{/list}">Ver listado</a> <br>
<a th:href="@{'/api/params/foo?message='+${user.name}}">Ver nombre en foo request param</a> <br>
<a th:href="@{/api/params/foo(message='Hola este es mi mensaje')}">Ver mensaje en foo request param</a> <br>
<a th:href="@{/api/params/foo-multiple(text='Hola este es mi mensaje', code=123)}">Ver mensaje en foo múltiple request param</a> <br>
```

* `${user.name}` es una variable del modelo.
* Los parámetros se agregan como parte de la URL (`?clave=valor`) usando `(...)` o concatenación.

### 🔹 Parámetros tipo *Path Variable*

```html
<a th:href="@{'/api/path/baz/'+${user.name}}">Ver nombre en baz path variable</a> <br>
<a th:href="@{/api/path/baz/Hola este es mi mensaje}">Ver mensaje en baz path variable</a> <br>
<a th:href="@{/api/path/baz-multiple/Manicho/70}">Ver mensaje en baz multiple path variable</a> <br>
```

* Las variables se insertan directamente en la ruta, respetando el orden del controlador.
* Ideal para rutas tipo RESTful (`/recurso/{valor}`).


## Resumen rápido

| Directiva  | Función |
|------------|---------|
| `th:text`  | Mostrar texto dinámico |
| `th:if`    | Mostrar contenido condicionalmente |
| `th:each`  | Iterar sobre listas |
| `th:unless`| Mostrar contenido si la condición es **falsa** |
| `th:switch` / `th:case` | Alternativa a múltiples `if` |
