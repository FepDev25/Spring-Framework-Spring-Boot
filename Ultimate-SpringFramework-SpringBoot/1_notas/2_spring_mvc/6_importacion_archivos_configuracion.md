# Importación de Archivos de Configuración con `@PropertySource` en Spring Boot

Spring Boot permite importar archivos de configuración personalizados (distintos de `application.properties`) mediante la anotación `@PropertySource`. Esto es útil para separar configuraciones específicas o sensibles.

### Ejemplo de Configuración

```java
package com.cultodeportivo.p1_springboot_web;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:values.properties", encoding = "UTF-8")
public class ValuesConfig {
}
```

### Explicación de los parámetros

- **`value`**: Indica la ubicación del archivo `.properties` dentro del classpath.  
  En este caso, se busca en `src/main/resources/values.properties`.

- **`encoding`**: Define el tipo de codificación de caracteres.  
  Se usa `"UTF-8"` para soportar caracteres especiales como la letra ñ o tildes.

---

### Ejemplo de archivo `values.properties`

```properties
config.code=25
config.username=Felipe
config.message=Hola le amo a mi novia Emi, mucho más ñ
config.esposa=Emilia
config.listOfValues=Bruno,Polita,Mikey,Perla
```

Este archivo puede ser inyectado posteriormente en cualquier `@Component` o `@Service` usando `@Value` o `@ConfigurationProperties`.

---

### Cargar múltiples archivos de propiedades

Si necesitas importar más de un archivo:

```java
@PropertySources({
    @PropertySource("classpath:values.properties"),
    @PropertySource("classpath:otro-archivo.properties")
})
```

---

Este enfoque es útil para centralizar configuraciones externas o dividirlas por contexto (como desarrollo, producción, etc.).
