package md.utm.cloudapp

import io.kubernetes.client.openapi.ApiClient
import io.kubernetes.client.openapi.apis.AppsV1Api
import io.kubernetes.client.util.Config
import io.kubernetes.client.openapi.Configuration
import io.kubernetes.client.util.ClientBuilder

class KuberManager {
    private val api: AppsV1Api
    private val client: ApiClient = ClientBuilder.cluster().build()
    private val namespace = "default"
    private val deploymentName = "cloud-app-deploy"
    private val containerName = "cloud-app"

    init {
        Configuration.setDefaultApiClient(client)
        api = AppsV1Api()
    }

    fun updateDeploymentImage(newImage: String): Boolean {
        val patch = """{"spec": {"template": { "spec": {"containers": [{ "name": "$containerName", "image": "$newImage"}]}}}}"""
        api.patchNamespacedDeployment(deploymentName, namespace, io.kubernetes.client.custom.V1Patch(patch), null, null, null, null, true)
        println("Updated image of container '$containerName' in deployment '$deploymentName' to '$newImage'")
        return true
    }

}