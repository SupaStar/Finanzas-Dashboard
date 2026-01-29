# Stockerly

Todo empezó con un archivo de Excel sobrecargado. A medida que mi portafolio crecía, mis herramientas se quedaban atrás; cálculos simples, como el total de rentas cobradas, se volvieron lentos y tediosos. Me di cuenta de que no podía tomar decisiones rápidas con herramientas lentas. Así nació Stockerly: un proyecto que deja atrás los scripts pesados para ofrecer una gestión de acciones fluida, precisa y en tiempo real.

## Version productiva compartida

Puedes encontrar una version productiva en el [Link](http://3.144.13.186:8082/)

## Ejecucion local

### Backend

Para poder ejecutar el proyecto, es necesario el uso de java, ya que esta implementado en springboot
Modificar el finanzas-back\src\main\resources\application-prod.properties

## Ejecución de los Proyectos

### Backend (finanzas-back)

1. **Requisitos previos:**

   - Java 17 o superior instalado.
   - Gradle configurado en el sistema.
2. **Pasos para ejecutar:**

   - En macOS/Linux:
     ```shell
     ./gradlew bootRun
     ```
   - En Windows:
     ```shell
     .\gradlew.bat bootRun
     ```
3. **Configuración:**

   - Los archivos de configuración se encuentran en `src/main/resources/`.
     - `application-dev.properties`: Configuración para desarrollo.
     - `application-prod.properties`: Configuración para producción.

### Frontend (FinanzasDashFront)

1. **Requisitos previos:**

   - Node.js y npm instalados.
2. **Pasos para ejecutar:**

   - En macOS/Linux:
     ```shell
     npm install
     npm start
     ```
   - En Windows:
     ```shell
     npm install
     npm start
     ```

### Redis y BD

Para la ejecucion de estos debes hacerlos por los metodos de cada una de las documentaciones

#### Pystock

```
pip install -r requirements.txt
python app/main.py
```


### Ejecución con Docker

1. **Requisitos previos:**

   - Docker y Docker Compose instalados.
2. **Pasos para ejecutar:**

   - Construir y levantar los contenedores:
     ```shell
     docker-compose up --build
     ```
   - Esto levantará tanto el backend como el frontend en contenedores separados.
3. **Configuración:**

   - El archivo `docker-compose.yml` contiene la configuración de los servicios.
   - Asegúrate de que las variables de entorno necesarias estén configuradas correctamente.

---

Con estos pasos, puedes ejecutar los proyectos individualmente o mediante Docker para un entorno integrado.
