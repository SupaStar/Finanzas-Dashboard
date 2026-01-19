Pushear imagen


# Latest

docker build -t finanzas-frontend .

docker tag finanzas-frontend:latest imsupastar222/finanzas-frontend:latest

docker push imsupastar222/finanzas-frontend:latest

# Oracle

docker build -t finanzas-frontend:oracle  .

docker tag finanzas-frontend:oracle imsupastar222/finanzas-frontend:oracle 

docker push imsupastar222/finanzas-frontend:oracle 

En servidor EC2

docker pull imsupastar222/finanzas-frontend:latest

Validar docker-compose.yml
frontend:
image: imsupastar222/finanzas-frontend:oracle
container_name: finanzas-dash-frontend
restart: always
ports:

- "8082:3999"
  networks:
- finanzas-network
  depends_on:
- backend

Los demas quedan como
docker compose build py-stock
docker compose build backend

docker compose up -d

