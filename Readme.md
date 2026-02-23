# Stockerly

Todo empezó con un archivo de Excel sobrecargado. A medida que mi portafolio crecía, mis herramientas se quedaban atrás; cálculos simples, como el total de rentas cobradas, se volvieron lentos y tediosos. Me di cuenta de que no podía tomar decisiones rápidas con herramientas lentas. Así nació Stockerly: un proyecto que deja atrás los scripts pesados para ofrecer una gestión de acciones fluida, precisa y en tiempo real.

## Versión Productiva

Puedes encontrar una versión productiva en: [Stockerly Live](http://3.144.13.186:8082/)

## Arquitectura

El proyecto se compone de los siguientes servicios:

*   **Frontend (FinanzasDashFront):** Aplicación desarrollada en Kotlin Multiplatform (Compose). Soporta Web (Wasm/JS), Desktop, Android e iOS.
*   **Backend (finanzas-back):** API REST desarrollada en Java con Spring Boot.
*   **Py-Stock:** Microservicio en Python (FastAPI) para obtener datos bursátiles.
*   **Base de Datos:** PostgreSQL.
*   **Cache/Mensajería:** Redis.

## Requisitos Previos

Para ejecutar el proyecto localmente, necesitas tener instalado:

*   **Java JDK 17** o superior.
*   **Docker y Docker Compose** (Recomendado para la ejecución integrada).
*   **Python 3.9+** (Para ejecutar `py-stock` localmente).
*   **Node.js** (Opcional, Gradle gestiona las dependencias de Node para el frontend web, pero es útil tenerlo instalado).

## Configuración del Entorno

Antes de ejecutar cualquier servicio, asegúrate de configurar las variables de entorno.

1.  Copia el archivo de ejemplo:
    ```bash
    cp .env.example .env
    ```
2.  Edita el archivo `.env` si es necesario (generalmente la configuración por defecto funciona para desarrollo local con Docker).

## Ejecución con Docker (Recomendado)

Esta es la forma más sencilla de levantar todo el entorno (Base de datos, Backend, Frontend, Redis, Py-Stock).

1.  Construir y levantar los servicios:
    ```bash
    docker-compose up --build
    ```
2.  Acceder a la aplicación:
    *   Frontend: `http://localhost:8082`
    *   Backend: `http://localhost:8080`

Para detener los servicios:
```bash
docker-compose down
```

## Ejecución Manual (Local)

Si prefieres ejecutar cada servicio individualmente, sigue estos pasos.

### 1. Base de Datos y Redis

Es necesario tener PostgreSQL y Redis corriendo. Puedes usar Docker para esto:

```bash
# Redis
docker run -d --name finanzas-dash-redis -p 6379:6379 redis:7

# PostgreSQL
docker run -d --name finanzas-dash-postgres -p 5432:5432 \
  -e POSTGRES_USER=finanzas_admin \
  -e POSTGRES_PASSWORD=esto_es_un_secreto \
  -e POSTGRES_DB=finanzas \
  postgres:15
```

### 2. Backend (finanzas-back)

1.  Navega al directorio:
    ```bash
    cd finanzas-back
    ```
2.  Ejecuta la aplicación usando Gradle:
    *   **macOS/Linux:**
        ```bash
        ./gradlew bootRun
        ```
    *   **Windows:**
        ```cmd
        .\gradlew.bat bootRun
        ```
    *Nota: La configuración se carga de `src/main/resources/application-dev.properties` por defecto en entorno local.*

### 3. Microservicio de Stock (py-stock)

1.  Navega al directorio:
    ```bash
    cd py-stock
    ```
2.  Crea un entorno virtual (opcional pero recomendado):
    ```bash
    python -m venv venv
    source venv/bin/activate  # En Windows: venv\Scripts\activate
    ```
3.  Instala las dependencias:
    ```bash
    pip install -r requirements.txt
    ```
4.  Ejecuta el servicio con Uvicorn:
    ```bash
    uvicorn app.main:app --reload --host 0.0.0.0 --port 8001
    ```

### 4. Frontend (FinanzasDashFront)

El frontend es un proyecto Kotlin Multiplatform. Puedes ejecutarlo en diferentes objetivos (Web, Desktop).

1.  Navega al directorio:
    ```bash
    cd FinanzasDashFront
    ```

#### Web (Wasm - Recomendado para navegadores modernos)
*   **macOS/Linux:**
    ```bash
    ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
    ```
*   **Windows:**
    ```cmd
    .\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
    ```

#### Web (JS - Compatible con navegadores antiguos)
*   **macOS/Linux:**
    ```bash
    ./gradlew :composeApp:jsBrowserDevelopmentRun
    ```
*   **Windows:**
    ```cmd
    .\gradlew.bat :composeApp:jsBrowserDevelopmentRun
    ```

#### Desktop (JVM)
*   **macOS/Linux:**
    ```bash
    ./gradlew :composeApp:run
    ```
*   **Windows:**
    ```cmd
    .\gradlew.bat :composeApp:run
    ```

---

¡Disfruta usando Stockerly!
