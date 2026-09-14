# EconoList

Aplicación web académica para comparar precios de productos entre supermercados españoles y ayudar a construir una compra más económica.

El proyecto consta de una API REST en Spring Boot y un cliente web en Vaadin. Cada supermercado se integra mediante un *finder* independiente; los resultados se normalizan para consultar productos, comparar precios y mostrar ofertas.

> Proyecto grupal desarrollado para la asignatura Proyectos 2 de la Universidad Francisco de Vitoria. El código se publica como muestra de portfolio académico; no representa un servicio de producción ni garantiza la disponibilidad de las fuentes de los supermercados.

## Funcionalidades

- Búsqueda de productos en varios supermercados.
- Comparación de precios de un producto.
- Vista de ofertas y carrito/lista de compra.
- Registro e inicio de sesión de usuarios.
- API documentada con OpenAPI/Swagger.

## Stack

- **Backend:** Java 17, Spring Boot, Spring Data JPA, PostgreSQL y OpenAPI.
- **Frontend:** Java 17, Vaadin y Spring Boot.
- **Infraestructura:** Docker Compose y Maven.

## Arquitectura

```text
front (Vaadin, :8082)  --->  back (Spring Boot API, :8089)  --->  PostgreSQL
                                      |
                                      +-- Finders de supermercados
```

## Ejecutar en local

Necesitas Java 17+ y Docker Desktop (para la opción con contenedores).

1. Crea tu configuración local desde el ejemplo:

   ```bash
   cp .env.example .env
   ```

   En Windows PowerShell:

   ```powershell
   Copy-Item .env.example .env
   ```

2. Arranca los servicios:

   ```bash
   docker compose up --build
   ```

3. Abre `http://localhost:8082`. La API se expone en `http://localhost:8089`.

Para detenerlos:

```bash
docker compose down
```

Las URLs de proveedores externos y sus respuestas pueden cambiar con el tiempo; por ello alguna búsqueda puede dejar de estar disponible sin que el resto de la aplicación falle.

## Desarrollo sin Docker

Con PostgreSQL disponible en local y las variables indicadas en `.env.example`, puedes iniciar los módulos por separado:

```bash
cd back
./mvnw spring-boot:run
```

En otra terminal:

```bash
cd front
./mvnw spring-boot:run
```

En Windows sustituye `./mvnw` por `mvnw.cmd`.

## Documentación

- [Manual de usuario](docs/user-manual.pdf) .
- `docs/project-plan.gan`: planificación original en GanttProject.
