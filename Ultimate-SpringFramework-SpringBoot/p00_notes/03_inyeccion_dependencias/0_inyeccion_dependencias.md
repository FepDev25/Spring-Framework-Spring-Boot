# Inyección de dependencias

- Proveer o suministrar a un objeto una referencia de otro objeto.
- Resuelve el problema de reutilización y modularidad.
- Ambos deben estar registrados.
- En contra pocisión de la creación explícita (operadores new) de objetos.
- Esto permite un bajo acoplamiento entre los objetos.
- Se pueden plasmar mediante la anotación @Autowired.

## Anotación Autowired

- Especifica que se inyectará un objeto de spring (componente) en un atributo de otro objeto.
- La inyección falla si no se encuentra candidatos disponibles.
- ![Autowired en atributo](./img/1.png)
- ![Autowired en set](./img/2.png)
- ![Autowired en constructor](./img/3.png)

## Definir componentes con clase Configuration

- ![Clase Configuration con beans](./img/4.png)
