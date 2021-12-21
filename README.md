# RSO PROJECT 2021: Suggestions microservice

## Prerequisites

```bash
docker run -d --name pg-suggestions -e POSTGRES_USER=kolan51 -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=suggestions -p 5432:5432 postgres:13
```

```
CREATE TABLE customers ( id serial PRIMARY KEY, firstName VARCHAR ( 50 ) NOT NULL, lastName VARCHAR ( 50 ) NOT NULL, streetAddress VARCHAR ( 50 ) NOT NULL, postcode real NOT NULL, birthDate VARCHAR (11) NOT NULL );
```

## Build and run commands
```bash
mvn clean package
cd api/target
java -jar suggestions-api-1.0.0-SNAPSHOT.jar
```
Available at: localhost:8080/v1/suggestions

## Docker commands
```bash
docker build -t novaslika .   
docker images
docker run novaslika    
docker tag novaslika prporso/novaslika   
docker push rso2021/novaslika  
```

## Docker and environmental variables
```bash
docker run --help
docker run -e MY_VAR=123
docker ps
docker build -t rso-dn
docker network ls
docker network create rso
docker network rm rso
docker rm -f pg-image-metadata
docker run -d --name pg-suggestions -e POSTGRES_USER=kolan51 -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=suggestions -p 5432:5432 --network rso postgres:13
docker inspect pg-image-metadata
docker run -p 8080:8080 --network rso -e KUMULUZEE_DATASOURCES0_CONNECTIONURL=jdbc:postgresql://pg-suggestions:5432/suggestions rso-dn
```

## Consul
```bash
consul agent -dev
```
Available at: localhost:8500


## Kubernetes
```bash
kubectl version
kubectl --help
kubectl get nodes
kubectl create -f suggestions-deployment.yaml 
kubectl apply -f suggestions-deployment.yaml 
kubectl get services 
kubectl get deployments
kubectl get pods
kubectl logs suggestions-deployment-6f59c5d96c-rjz46
kubectl delete pod suggestions-deployment-6f59c5d96c-rjz46
```

```
https://docs.microsoft.com/en-us/rest/api/maps/weather
https://azure.microsoft.com/en-us/services/azure-maps/
```
Secrets: https://kubernetes.io/docs/concepts/configuration/secret/
