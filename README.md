# Todolist Demo Application
> This documentation is gracefully generated with AI and attentively reviewed and checked by humans =)

This demo application is structured with a simple three-tier architecture:
- A **reverse proxy** (Apache HTTPD) handles incoming HTTP(S) traffic, provides SSL termination, and forwards requests to the backend.
- A **Spring Boot application** (todo-app) processes the core logic and APIs.
- A **PostgreSQL database** provides persistent storage.
- The **frontend** (not included in this demo) would typically be a separate service, possibly running on a different port or domain, and would communicate with the backend via REST APIs.

> This setup mirrors typical production deployments with separation of concerns, allowing flexible scalability and security hardening for each layer.

---
# Setup Guide
This guide will help you set up and run the **Todolist Demo Application** with Docker, including environment configuration, HTTPS setup, troubleshooting, container naming conventions, and a sample `docker-compose.yml` configuration.
## 0. Build the Spring Boot Application

The project is organized as follows:
- `infrastructure/` contains all the **virtualization and configuration stuff** setup, including `docker-compose.yml`, SSL certs, HTTPD configuration, and supporting files.
- `src/` contains the **Spring Boot application**, which you need to build and containerize before running the full stack.

Before running the application with Docker, build the Spring Boot backend and ensure configuration properties are correctly defined.

### Steps:

1. **Check `application.yml` or `application.properties`**  
   Ensure values such as database host, port, credentials, and `SPRING_PROFILES_ACTIVE` match what is defined in `.env`.

2. **Build the JAR**
```bash
./mvnw clean package -DskipTests
```
The resulting JAR file should appear in the `target/` directory (e.g., `target/todo-app.jar`).

3. **Build the Docker Image**  
   Ensure your Dockerfile is in the root of the Spring Boot project and run:
```bash
docker build -t eneabette/todo-app:latest .
```
This will be used by the `todo-app` service in `docker-compose.yml`.

---

## 1. Environment Configuration (`.env` file)

Create a `.env` file in the root directory of the project. This file contains environment variables used by the Docker containers.

### Sample `.env` content:
```env
POSTGRES_USER=todolist_user
POSTGRES_PASSWORD=todolist_password
POSTGRES_DB=todolist_db
DB_HOST=postgresql
DB_PORT=5432
SPRING_PROFILES_ACTIVE=prod|dev
```

> 🔒 **Note:** Make sure to keep the `.env` file secure and do not commit it to version control.

---

## 2. Generate HTTPS Certificates for Apache HTTPD

The application uses Apache HTTPD with SSL. You must generate a self-signed certificate and place it in the correct directory.

### Steps to generate the certificate:
```bash
mkdir -p ./apache2/conf/ssl
openssl req -x509 -nodes -days 365 \
  -newkey rsa:2048 \
  -keyout ./apache2/conf/ssl/server.key \
  -out ./apache2/conf/ssl/server.crt \
  -subj "/CN=iceage.local"
```

### Placement:
Ensure the following paths match what's expected inside the container (referenced in `.env`):
- `./apache2/conf/ssl/server.crt` → `/usr/local/apache2/conf/ssl/server.crt`
- `./apache2/conf/ssl/server.key` → `/usr/local/apache2/conf/ssl/server.key`

Update your `docker-compose.yml` to mount the certs directory:
```yaml
volumes:
  - ./apache2/conf/ssl:/usr/local/apache2/conf/ssl:ro
```

---

## 3. Running the Application

From the project root, run:
```bash
docker-compose --env-file .env up --build
```
Then open [https://iceage.local](https://iceage.local) in your browser.

> ✅ Add an entry to your `/etc/hosts` file (for windows users use `\Windows\System32\drivers\etc\hosts`):
```plaintext
127.0.0.1 iceage.local
```
> 🔒 **Note:** If you are working with a self generated certificate you have to manually configure the entries on client and server side
---

## 4. Troubleshooting Connectivity Issues

This section lists known issues grouped by component (HTTPD, application server, and database), with concise explanations and resolutions.

### 🧩 Reverse Proxy (Apache HTTPD)

#### 🔧 Apache fails to start (certificate issues)
- **Error:** `AH00072: make_sock: could not bind to address`
- **Fix:** Ensure ports 80/443 are free. Check cert and key file paths and permissions.

#### 🧩 HTTPD config file errors
- **Error:** Config parsing errors on boot.
- **Fix:** Run `httpd -t` inside the container to check syntax. Ensure no missing includes.

#### 🔐 Browser shows warning or refuses to load site
- **Cause:** Using a self-signed certificate.
- **Fix:** Accept the certificate manually or add to your local trust store (for development).

#### 🔁 Proxy config changes don’t apply
- **Cause:** Config not reloaded or stale volume.
- **Fix:** Stop, rebuild, and restart the reverse proxy container. Confirm the file path is correctly mounted.

#### 🚫 Page not found / 404 or 502 errors
- **Cause:** Reverse proxy can’t reach backend.
- **Fix:** Check proxy target URLs and that `todo-app` is running and reachable by name.

### ⚙️ Application Server (Spring Boot `todo-app`)

#### 🔌 Frontend/API can’t reach backend
- **Cause:** Wrong host used (e.g., `localhost`).
- **Fix:** Use Docker Compose service names like `todo-app` for internal access.

#### ❌ Environment variables not loaded
- **Cause:** `.env` file syntax error or not passed to `docker-compose`.
- **Fix:** Validate `.env`, avoid quotes, and always run with `--env-file .env`.

#### 🧪 Code or config changes not applied
- **Cause:** Container uses cached image or volume.
- **Fix:** Run `docker-compose build` and `docker-compose down -v && up --build`.

#### 🐛 Spring Boot fails on startup
- **Cause:** Misconfigured DB credentials or missing properties.
- **Fix:** Check logs. Match `.env` values with application config.

### 🗄️ Database (PostgreSQL)

#### 🧱 `todo-app` fails to connect to DB
- **Cause:** Race condition or wrong host/port.
- **Fix:** Use `depends_on`. Ensure `DB_HOST=postgresql` and port 5432.

#### 📄 Postgres container loses data between runs
- **Cause:** Missing volume mount.
- **Fix:** Use `./pgdata:/var/lib/postgresql/data` in `docker-compose.yml`.

#### 🔑 Login/auth errors from `todo-app`
- **Cause:** Wrong credentials.
- **Fix:** Ensure the user, password, and DB name match across `.env`, `todo-app`, and `postgresql`.


---

## 5. Container Naming & DNS Resolution Tips

Docker Compose creates internal DNS entries for services using the **service names** defined in `docker-compose.yml`.

### Best practices:
- Match hostnames and environment variables exactly.
- Example:
```yaml
services:
  todolist_backend:
    container_name: todolist_backend
  todolist_frontend:
    container_name: todolist_frontend
```
- Refer to `todolist_backend` from the frontend code or configuration, not `localhost`.

---

## 6. Docker Compose Configuration

Below is the `docker-compose.yml` configuration used for the Todolist demo:

```yaml
services:
  env-check:
    image: eneabette/env-test:latest
    environment:
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      POSTGRES_DB: ${POSTGRES_DB}
      DB_HOST: ${DB_HOST}
      DB_PORT: ${DB_PORT}
      SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE}
    networks:
      - internal

  reverse-proxy:
    image: httpd:latest
    ports:
      - "80:80"
      - "443:443"   # External access via HTTP redirects to HTTPS
    volumes:
      - ./apache2/conf/httpd.conf:/usr/local/apache2/conf/httpd.conf:ro
      - ./apache2/conf/extra/reverse-proxy.conf:/usr/local/apache2/conf/extra/reverse-proxy.conf:ro
      - ./apache2/conf/extra/httpd-mod-ssl.conf:/usr/local/apache2/conf/extra/httpd-mod-ssl.conf:ro
      - ./apache2/conf/ssl:/usr/local/apache2/conf/ssl:ro
    depends_on:
      - env-check
      - todo-app
    networks:
      - internal

  todo-app:
    image: eneabette/todo-app:latest
    container_name: todo-app
    environment:
      JAVA_TOOL_OPTIONS: -Dspring.jpa.hibernate.ddl-auto=create-drop
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      POSTGRES_DB: ${POSTGRES_DB}
      DB_HOST: ${DB_HOST}
      DB_PORT: ${DB_PORT}
      SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE}
    depends_on:
      - env-check
      - postgresql
    networks:
      - internal
    # No published ports – accessible only via Docker networking

  postgresql:
    image: postgres:latest
    container_name: ${DB_HOST}
    environment:
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      POSTGRES_DB: ${POSTGRES_DB}
    volumes:
      - ./pgdata:/var/lib/postgresql/data
    networks:
      - internal
    depends_on:
      - env-check
    # No published ports – accessible only within the network

networks:
  internal:
    driver: bridge
```

### Comments:
- Environment variables are referenced from `.env`.
- The `reverse-proxy` service handles HTTPS via `httpd`.
- Certificates are mounted from `./apache2/conf/ssl`.
- The `env-check` is a utility service to validate variable propagation.
- `todo-app` is only reachable internally.
- `postgresql` uses the `DB_HOST` name for container resolution.

---

## Final Notes

- Be consistent with naming to avoid connectivity bugs.
- Ensure `.env`, certs, and `docker-compose.yml` are in sync.
- Use Docker logs (`docker-compose logs`) to inspect issues.
- To clean and rebuild: `docker-compose down -v && docker-compose up --build`

Happy hacking! 🚀
