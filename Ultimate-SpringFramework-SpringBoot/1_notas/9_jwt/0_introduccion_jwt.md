### **1. JSON Web Token (JWT)**

#### **Introducción**

El **JSON Web Token (JWT)** es un estándar abierto (RFC 7519) utilizado para implementar seguridad en aplicaciones web, especialmente en las **API RESTful**. Es un método compacto, autónomo y seguro para representar reclamaciones (claims) entre dos partes, generalmente un cliente y un servidor, de manera que ambas partes puedan verificar la autenticidad e integridad de la información intercambiada.

Los **JWT** se utilizan ampliamente en sistemas de autenticación y autorización debido a su naturaleza eficiente, escalable y fácil de usar, tanto en aplicaciones pequeñas como en arquitecturas distribuidas.

#### **Características Clave de JWT**

1. **Estándar Abierto para API REST**:
   JWT proporciona un mecanismo abierto y basado en estándares para la implementación de autenticación y autorización en aplicaciones web y móviles que exponen APIs REST. El uso de JWT elimina la necesidad de mantener un estado en el servidor, lo que lo hace ideal para aplicaciones distribuidas.

2. **Escalabilidad y Aplicaciones Independientes**:
   Las aplicaciones que implementan JWT son escalables y pueden operar de manera independiente. El token no requiere la sesión del servidor, lo que permite que las aplicaciones sean más fácilmente escalables y distribuidas a través de múltiples instancias o servidores.

3. **Codificación en Base 64**:
   El token JWT está codificado en **Base64 URL-safe**, lo que garantiza que puede ser transportado de manera segura en un URL o en un encabezado HTTP sin problemas de caracteres incompatibles.

4. **Contiene Reclamaciones (Claims)**:
   Un JWT puede contener varias **reclamaciones** (claims), que son las declaraciones sobre una entidad (generalmente el usuario) y datos adicionales que son útiles para la aplicación. Estas reclamaciones pueden incluir la identidad del usuario, roles, permisos, fechas de expiración, entre otras.

5. **Firmado con una Clave Secreta**:
   Para garantizar la integridad del token y que no haya sido alterado, JWT se firma digitalmente utilizando una **clave privada**. Solo quien tiene acceso a esta clave secreta puede generar y validar el token.

6. **Compacto**:
   JWT es muy compacto y liviano, lo que lo hace ideal para ser transmitido a través de HTTP, ya sea en la URL, el cuerpo de la solicitud, o los encabezados HTTP. La pequeña huella de los JWT los hace eficientes en entornos distribuidos y móviles.

7. **Autónomo**:
   A diferencia de otros mecanismos de autenticación, JWT es autónomo, lo que significa que contiene toda la información necesaria para autenticar al usuario y verificar su identidad sin necesidad de consultar el servidor o mantener el estado de la sesión.

8. **Seguridad**:
   La seguridad de JWT se basa en su firma digital. Si el JWT es manipulado de alguna forma, su firma se invalida y el servidor no podrá autenticarlo como válido. Además, la transmisión de estos tokens suele estar cifrada (usando HTTPS), lo que protege la integridad y la privacidad de los datos.

---

### **Flujo del Proceso de Autenticación con JWT**

#### **Inicio del Proceso de Autenticación**:

Cuando un usuario interactúa con una aplicación que requiere autenticación, el proceso sigue generalmente los siguientes pasos:

1. **Usuario Hace Login**:
   El usuario realiza una solicitud de inicio de sesión utilizando su **nombre de usuario** y **contraseña**.

   **Método**: POST
   **Ruta**: `/login`
   **Cuerpo de la Solicitud**:

   ```json
   {
       "username": "user1",
       "password": "password123"
   }
   ```

2. **Servidor Procesa el Login**:

   * El servidor recibe las credenciales y las valida a través de un filtro de seguridad (Spring Security, en el caso de una aplicación Spring Boot).
   * El servidor compara el **nombre de usuario** y **contraseña** con los almacenados en la base de datos.

   Si las credenciales son válidas, el servidor procederá a generar un token JWT, el cual contiene las **reclamaciones** necesarias (nombre de usuario, roles, fecha de expiración, etc.).

3. **Autenticación Exitosa**:

   * Si la autenticación es **exitosa**:

     * El servidor genera un **token JWT firmado** con una clave secreta.
     * El token JWT es enviado de vuelta al cliente en el cuerpo de la respuesta.
     * El cliente almacena el JWT (por ejemplo, en `localStorage`, `sessionStorage` o en una cookie).

   **Respuesta**:

   ```json
   {
       "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxMjM0NTY3ODkwLCJleHBpcmVkX3N0YXR1c19yZXNwb25zZV9pc3N1Z2lkIjp7fQ.JwLJ8P5R38n8RH1xnY7v4oBO0XwBl1wzsgJnt9ZTsm0"
   }
   ```

4. **Autenticación Fallida**:

   * Si las credenciales no son válidas, el servidor responde con un **401 Unauthorized**.

   **Respuesta**:

   ```json
   {
       "error": "Credenciales inválidas"
   }
   ```

---

#### **Acceder a un Recurso Protegido Usando el JWT**:

1. **Usuario Solicita un Recurso Protegido**:
   Para acceder a un recurso protegido, el cliente debe incluir el **token JWT** en el encabezado de autorización de la solicitud HTTP.

   **Método**: GET
   **Ruta**: `/api/protected-resource`
   **Encabezado de la Solicitud**:

   ```http
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxMjM0NTY3ODkwLCJleHBpcmVkX3N0YXR1c19yZXNwb25zZV9pc3N1Z2lkIjp7fQ.JwLJ8P5R38n8RH1xnY7v4oBO0XwBl1wzsgJnt9ZTsm0
   ```

2. **Servidor Valida el JWT**:

   * El servidor recibe la solicitud con el **token JWT** en el encabezado.
   * El servidor valida la firma del token utilizando la **clave secreta**.

     * Si la firma es válida y el token no ha expirado, el servidor procede con la solicitud.
     * Si la firma es inválida o el token ha expirado, el servidor responde con un **403 Forbidden**.

   **Validación de Token**:

   * **No válido o expirado**: `403 Forbidden`
   * **Válido**: El servidor extrae las reclamaciones del token y utiliza la información (como el nombre de usuario o los roles) para autorizar el acceso.

3. **Acceso al Recurso**:

   * Si el token es válido, el servidor verifica los permisos del usuario (por ejemplo, si tiene el rol necesario para acceder al recurso).
   * Si el usuario tiene acceso, se le devuelve el recurso protegido.

   **Respuesta**:

   ```json
   {
       "data": "Contenido confidencial"
   }
   ```

---

### **Diagrama del Proceso de Autenticación con JWT**

1. **Inicio del Proceso de Login**
2. **El Usuario Envía sus Credenciales** (Método POST: Login con username y password)
3. **Servidor Valida las Credenciales**:

   * **Autenticado?**

     * **Sí**: Generación y Envío del JWT al Cliente
     * **No**: 401 Unauthorized
4. **Acceder a un Recurso Protegido**:

   * **Envío del JWT en el encabezado `Authorization`**
5. **Servidor Valida el JWT**:

   * **Es Válido?**

     * **Sí**: Verifica Permisos y Roles, Acceso Concedido
     * **No**: 403 Forbidden

---

### **Ventajas del Uso de JWT**

1. **Autónomos**: Los tokens contienen toda la información necesaria para la validación, lo que elimina la necesidad de sesiones en el servidor.
2. **Escalables**: Debido a que el token no depende de un estado centralizado, la infraestructura se vuelve más escalable y fácil de manejar.
3. **Compactos**: Al ser pequeños, los JWTs son eficientes en términos de ancho de banda.
4. **Seguridad**: El uso de firmas y encriptación asegura la integridad y la privacidad del token.

En resumen, **JWT** es una excelente herramienta para la autenticación y autorización en sistemas distribuidos, escalables y sin estado, lo que lo hace ideal para aplicaciones modernas basadas en microservicios.
