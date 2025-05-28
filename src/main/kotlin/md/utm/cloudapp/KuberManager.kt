package md.utm.cloudapp

import io.kubernetes.client.openapi.ApiClient
import io.kubernetes.client.openapi.apis.AppsV1Api
import io.kubernetes.client.util.Config
import io.kubernetes.client.openapi.Configuration

class KuberManager {
    private val api: AppsV1Api = AppsV1Api()
    private val client: ApiClient = Config.fromCluster().apply {
        basePath = "https://kubernetes.default.svc:443"
    }
    private val namespace = "default"
    private val deploymentName = "cloud-app-deploy"
    private val containerName = "cloud-app"

    init {
        Configuration.setDefaultApiClient(client)
    }

    fun updateDeploymentImage(newImage: String): Boolean {

        val deployment = api.readNamespacedDeployment(deploymentName, namespace, null)

        val containers = deployment.spec?.template?.spec?.containers
        val target = containers?.firstOrNull { it.name == containerName }

        if (target == null) {
            println("Container $containerName not found in $deploymentName")
            return false
        }

        target.image = newImage
        api.replaceNamespacedDeployment(deploymentName, namespace, deployment, null, null, null, null)
        println("Updated image of container '$containerName' in deployment '$deploymentName' to '$newImage'")
        return true
    }

}