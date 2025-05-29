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

### Configure autoscale of deployment

```
...
kubectl autoscale deployment cloud-app-deploy --min=2 --max=10 
```

## Github Actions

### Create Action to Deploy new image to Dockerhub and then update the local cluster

```
name: Create Publish Docker Image

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

jobs:

  build:

    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v4
    
    - name: Log in to DockerHub
      uses: docker/login-action@v3
      with:
        username: ${{ secrets.DOCKER_USERNAME }}
        password: ${{ secrets.DOCKER_PASSWORD }}
        
    - name: Build the Docker image
      run: docker build . --file Dockerfile --tag crickitycrick/cloud-app:${{ github.run_number }}

    - name: Publish Docker Image
      run: docker push crickitycrick/cloud-app:${{ github.run_number }}

    - name: Trigger Kubernetes update
      run: curl -X POST https://b5a2-89-28-75-176.ngrok-free.app/update/${{ github.run_number }}

```

Inside the server created a new route: /update/version, which directs towards:

```
fun updateDeploymentImage(newImage: String): Boolean {
        val patch = """[{ "op" : "replace", "path": "/spec/template/spec/containers/0/image", "value": "$newImage"}]"""
        try {
            PatchUtils.patch(
            V1Deployment::class.java,
            {api.patchNamespacedDeploymentCall(deploymentName, namespace, io.kubernetes.client.custom.V1Patch(patch), null, null, null, null, null, null) },
            V1Patch.PATCH_FORMAT_JSON_PATCH,
            api.apiClient
        ) } catch (e: ApiException) {
            println("Status code: ${e.code}")
            println("Response body: ${e.responseBody}")
            println("Response headers: ${e.responseHeaders}")
            e.printStackTrace()
        }
```

To make the pod capable of reaching Kubernetes engine from inside needs a role:

```
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: deployment-updater
  namespace: default
rules:
  - apiGroups: ["apps"]
    resources: ["deployments"]
    verbs: ["get", "update", "patch"]

---
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: deployment-updater-binding
  namespace: default
roleRef:
  kind: Role
  name: deployment-updater
  apiGroup: rbac.authorization.k8s.io
subjects:
  - kind: ServiceAccount
    name: default
    namespace: default

```

which can be applied with:

```
kubectl apply -f rbac.yaml
```

As a consideration, because in the current state update brings down all pods and may accidentally cause a slight second outage:
```
kubectl patch deployment cloud-app-deploy --type=strategic -p "{\"spec\":{\"strategy\":{\"rollingUpdate\":{\"maxSurge\":1,\"maxUnavailable\":0}}}}"
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

 