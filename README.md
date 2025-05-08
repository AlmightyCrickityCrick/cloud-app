# Getting Started

### Running the Application

```
./gradlew bootRun
```

Open [http://localhost:8080](http://localhost:8080) in your browser.

### Building the Application

```
./gradlew bootJar
```

### Command for using generated JAR file in Dockerfile

```
...
java -jar ./build/libs/cloud-app-0.0.1-SNAPSHOT.jar
```
### Running the Application as a Docker Container

```
...
docker build -t crickitycrick/cloud-app:v2 .
docker run -p 8080:8080 crickitycrick/cloud-app:v2
```

### DockerHub Repository

```
...
https://hub.docker.com/repository/docker/crickitycrick/cloud-app/tags
```

### Running as a Kubernetes Cluster

```
...
kubectl create deployment cloud-app-deploy --image=crickitycrick/cloud-app:latest
kubectl expose deployment cloud-app-deploy --type=LoadBalancer --port=8080 --target-port=8080
kubectl port-forward service/cloud-app-deploy 8080:8080
```

### Change number of replicas in Deployment

```
...
kubectl scale deployments/cloud-app-deploy --replicas=4
```

### Update the application without downtime

```
...
kubectl set image deployments/cloud-app-deploy cloud-app=crickitycrick/cloud-app:v2
## Verify rollback progress
kubectl rollout status deployments/cloud-app-deploy
```

### Rollback to the previous version

```
...
kubectl rollout undo deployments/cloud-app-deploy
```


### Requirements

1. This project should be made to run as a Docker image.
2. Docker image should be published to a Docker registry.
3. Docker image should be deployed to a Kubernetes cluster.
4. Kubernetes cluster should be running on a cloud provider.
5. Kubernetes cluster should be accessible from the internet.
6. Kubernetes cluster should be able to scale the application.
7. Kubernetes cluster should be able to update the application without downtime.
8. Kubernetes cluster should be able to rollback the application to a previous version.
9. Kubernetes cluster should be able to monitor the application.
10. Kubernetes cluster should be able to autoscale the application based on the load.

### Additional
1. Application logs should be stored in a centralised logging system (Loki, Kibana, etc.)
2. Application should be able to send metrics to a monitoring system.
3. Database should be running on a separate container.
4. Storage should be mounted to the database container.
