# Redirect & Forward en Spring MVC

En Spring MVC, cuando un controlador retorna una cadena con `"redirect:"` o `"forward:"`, está indicando cómo debe comportarse la navegación del usuario entre rutas. A continuación se detallan las diferencias entre ambos mecanismos.

---

## 🔁 `forward:/ruta`

- Se mantiene dentro **de la misma petición HTTP**.
- **No cambia** la URL en el navegador.
- **Conserva los atributos** de la petición original (útil para compartir datos entre controladores).
- No hace un refresh del navegador.
- Se utiliza cuando queremos **despachar internamente** a otra ruta o vista.

### Ejemplo:

```java
@Controller
public class HomeController {

    @GetMapping({"", "/", "/home"})
    public String home() {
        return "forward:/details";
    }
}
```

---

## 🔃 `redirect:/ruta`

- Genera una **nueva petición HTTP** (nuevo request-response).
- **Sí cambia** la URL en el navegador.
- **No conserva los atributos** del request original.
- Hace un refresh de la página.
- Se utiliza cuando queremos redirigir al cliente a una nueva ruta, por ejemplo, después de un formulario (`POST-Redirect-GET` pattern).

### Ejemplo:

```java
@Controller
public class HomeController {

    @GetMapping({"", "/", "/home"})
    public String home() {
        return "redirect:/details";
    }
}
```

---

## 📌 Comparación Rápida

| Característica         | `forward:`            | `redirect:`            |
|------------------------|------------------------|-------------------------|
| Misma petición         | ✅ Sí                  | ❌ No                   |
| Cambia URL navegador   | ❌ No                  | ✅ Sí                   |
| Conserva parámetros    | ✅ Sí                  | ❌ No                   |
| Refresh del navegador  | ❌ No                  | ✅ Sí                   |
| Uso típico             | Enrutamiento interno   | Redirección de flujo    |

---

## 🎯 Conclusión

- Usa `forward:` si necesitas pasar datos y mantener el contexto dentro de una misma petición.
- Usa `redirect:` cuando necesites redirigir al cliente, como después de un `POST`, o para evitar reenvíos accidentales con `F5`.
