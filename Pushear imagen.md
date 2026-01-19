Pushear imagen
docker build -t finanzas-frontend .

docker tag finanzas-frontend:latest imsupastar222/finanzas-frontend:latest

docker push imsupastar222/finanzas-frontend:latest

En servidor EC2

docker pull imsupastar222/finanzas-frontend:latest

Validar docker-compose.yml
 frontend:
    image: imsupastar222/finanzas-frontend:latest
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