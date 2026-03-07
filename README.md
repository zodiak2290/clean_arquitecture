# Enterprise Clean API

Esta es una API REST de ejemplo para un proyecto empresarial con **arquitectura limpia (Clean Architecture / DDD)** y contiene todo lo necesario para desplegarse mediante `docker-compose`.

## 📁 Estructura del código

- `domain`: modelos de dominio y repositorios de la capa de negocio.
- `application/usecase`: casos de uso que ejecutan la lógica de negocio.
- `infrastructure`: adaptadores externos (repositorios JPA, cache, almacenamiento, mensajería, seguridad, etc.).
- `interfaces/rest`: controladores y manejadores de excepciones.
- `config`: beans y configuración de Spring.

## 🧩 Principales componentes

- **Seguridad**: JWT mediante `JwtAuthFilter` y configuración en `SecurityConfig`.
- **Excepciones globales**: `ApiExceptionHandler` con respuestas homogéneas.
- **Caching**: interfaz `CacheService` y `InMemoryCacheService` (TTL simulado).
- **Logging**: logback con encoder JSON y MDC (`CorrelationIdFilter`, `UserMdcFilter`).
- **Base de datos**: Postgres con JPA (`SpringDataUserRepository`, `JpaUserRepository`).
- **Almacenamiento de archivos**: local o S3 según configuración (`LocalFileStorage`, `S3FileStorage`).
- **Documentos y eventos**: eventos publicados tras operaciones (implementación simple).

## 🚀 Levantar la aplicación localmente

### 1. Requisitos previos

- Docker & Docker Compose
- Java 17 (solo para compilar si no usas conteno­rizado)
- Maven 3.9 (opcionales; el `Dockerfile` se encarga de ello)

### 2. Variables de entorno

Puedes copiar las que aparecen en `docker-compose.yml` o exportarlas:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/enterprise_db
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=1234567

export APP_STORAGE_PROVIDER=local
export APP_STORAGE_BUCKET=
export APP_AWS_REGION=us-west-2

export AWS_ACCESS_KEY_ID=...
export AWS_SECRET_ACCESS_KEY=...
```

### 3. Ejecutar con Docker Compose

```bash
cd enterprise-clean-api
docker compose up --build
```

Esto crea:

- Un servicio `db` (Postgres 17).
- Dos réplicas `app1` y `app2` ejecutando la API.
- Un `nginx` que actúa como balanceador en el puerto 8080.
- Prometheus y Grafana para métricas.

La API queda accesible en `http://localhost:8080`.

### 4. Endpoints útiles

- `POST /auth/login` – obtiene JWT (imaginario, depende de implementación).
- `GET /users/{id}` – obtiene la entidad (se comprueba primero el caché Redis/in‑memory, si no está se lee de la BD y se almacena).
- Actuator: `/actuator/health`, `/actuator/prometheus`.

## 📦 Dockerfile y Compose

- El `Dockerfile` usa *multi-stage* para compilar con Maven y luego ejecutar una imagen `temurin:17-jre` ligera.
- `docker-compose.yml` orquesta DB, API, balanceador y métricas, demostrando escalabilidad básica.

## ☁️ Integración con AWS

Configuración para usar S3 como backend de archivos, parametrizada con `APP_STORAGE_PROVIDER` y credenciales normales de AWS.

## 📝 Notas para certificación AWS

La solución puede desplegarse en ECS/EKS con AWS RDS/Aurora; el diseño de capas facilita el cambio de infraestructura.

## 📌 Mejoras posibles

- Implementar TTL real y/o usar Redis/Caffeine.
- Clúster de Postgres (Master/Replica) o servicio gestionado para bases distribuidas.
- Más documentación y diagramas (este README ya lo es, pero siempre se puede ampliar).

---

¡Listo! Con este README ya tienes una documentación técnica profesional básica que describe el proyecto y cómo operar la API.
