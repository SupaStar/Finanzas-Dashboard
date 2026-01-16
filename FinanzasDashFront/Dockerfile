# =========================
# Etapa 1: Build
# =========================
FROM gradle:8.13-jdk21 AS build

USER root
RUN apt-get update && \
    apt-get install -y libatomic1 && \
    rm -rf /var/lib/apt/lists/*

USER gradle
ENV GRADLE_USER_HOME=/home/gradle/.gradle

WORKDIR /app

# -------------------------------------------------
# 1️⃣ Copiamos SOLO archivos de Gradle (CACHE)
# -------------------------------------------------
COPY --chown=gradle:gradle \
    settings.gradle* \
    build.gradle* \
    gradle.properties \
    gradlew \
    gradle/ \
    ./

# Descarga dependencias (se cachea esta capa)
RUN gradle dependencies --no-daemon || true

# -------------------------------------------------
# 2️⃣ Copiamos el resto del proyecto
# -------------------------------------------------
COPY --chown=gradle:gradle . .

# Build real
RUN gradle :composeApp:wasmJsBrowserDistribution --no-daemon

# =========================
# Etapa 2: Runtime
# =========================
FROM nginx:1.27-alpine AS runtime

RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf

WORKDIR /usr/share/nginx/html

COPY --from=build /app/composeApp/build/dist/wasmJs/productionExecutable/ .

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
