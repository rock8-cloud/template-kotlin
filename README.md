# Kotlin Gradle Postgres — Template

REST API backend built with **Kotlin 1.9**, **Spring Boot 3.4.1**, and **PostgreSQL**. Intended as a starting point for Kubernetes-deployed services: one table, one API endpoint, full Docker support out of the box.

**Tech stack:**

| Layer | Technology |
|---|---|
| Language | Kotlin 1.9.25 (JVM 21) |
| Framework | Spring Boot 3.4.1 |
| Build | Gradle 8 |
| Database | PostgreSQL |
| Migrations | Flyway |
| ORM | Spring Data JPA / Hibernate |
| Connection pool | HikariCP |
| API docs | springdoc-openapi 2.7 (Swagger UI) |

The application starts on port **8080**. Opening `/` in a browser redirects to the Swagger UI at `/swagger-ui/index.html`.

---

## Project Structure

```
src/main/kotlin/com/vibeham/template/
├── TemplateApplication.kt                   # Spring Boot entry point
├── config/
│   └── DataSourceConfig.kt                  # Custom DataSource bean
├── controller/
│   ├── HomeController.kt                    # GET / → redirect to Swagger UI
│   ├── InfoController.kt                    # GET /api/info
│   └── SpaceTargetController.kt             # GET /api/targets
├── model/
│   └── SpaceTarget.kt                       # JPA entity: id, name, type, distanceLightYears
└── repository/
    └── SpaceTargetRepository.kt             # Spring Data JPA repository

src/main/resources/
├── application.yml                          # Spring / JPA / Flyway / Swagger config
└── db/migration/
    └── V1__init.sql                         # Creates space_target table + seed data
```

**Key design decisions:**

- `DataSourceConfig` manually constructs the HikariCP `DataSource` so the app accepts both a single `DATABASE_URL` (the format issued by most managed cloud databases) and individual `DB_*` variables. Spring's auto-configuration for `DataSource` is intentionally bypassed.
- `ddl-auto: validate` — Hibernate only validates that the schema matches the entities. All schema changes go through Flyway migrations in `db/migration/`.
- `SpaceTarget` is the demo entity. To add your own domain: create a new entity in `model/`, a repository in `repository/`, a Flyway migration in `db/migration/`, and a controller in `controller/`. The demo files can be deleted once you no longer need them.
- `build.gradle.kts` disables the plain jar task so `build/libs/` contains only the fat jar produced by `bootJar`, which simplifies the `COPY *.jar` step in the Dockerfile.
- The Dockerfile uses a **multi-stage build**: the first stage (`gradle:8-jdk21`) compiles and packages; the second stage (`eclipse-temurin:21-jre-alpine`) copies only the fat JAR, keeping the final image small.

---

## Environment Variables

The application runs on port **8080** and requires a PostgreSQL database.

Database connection is configured via **one of two approaches**:

### Option A — Single connection URL (recommended for managed services)

| Variable | Required | Example |
|---|---|---|
| `DATABASE_URL` | yes | `postgres://user:pass@host:5432/dbname?sslmode=require` |

The URL must use the `postgres://` or `postgresql://` scheme. The `sslmode` query parameter is optional.

### Option B — Individual variables

| Variable | Required | Default | Description |
|---|---|---|---|
| `DB_HOST` | no | `localhost` | Database host |
| `DB_PORT` | no | `5432` | Database port |
| `DB_NAME` | no | `spacedb` | Database name |
| `DB_USER` | no | `postgres` | Database user |
| `DB_PASSWORD` | no | `postgres` | Database password |
| `DB_SSL_MODE` | no | _(none)_ | SSL mode, e.g. `require` or `disable` |

`DATABASE_URL` takes precedence. If it is set, the individual `DB_*` variables are ignored.

---

## Running Locally

### With Docker Compose

```bash
docker compose up
```

Starts the application together with a PostgreSQL instance and sets `DATABASE_URL` automatically. No additional configuration needed.

### Standalone

```bash
# build
./gradlew bootJar

# run — Option A
DATABASE_URL=postgres://postgres:postgres@localhost:5432/spacedb java -jar build/libs/*.jar

# run — Option B
DB_HOST=localhost DB_USER=postgres DB_PASSWORD=secret java -jar build/libs/*.jar
```
