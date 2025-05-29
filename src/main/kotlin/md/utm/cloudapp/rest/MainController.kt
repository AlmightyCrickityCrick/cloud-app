package md.utm.cloudapp.rest

import md.utm.cloudapp.KuberManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {
    private val manager = KuberManager()

    @GetMapping("/")
    fun main(): String {
        return "Why are we here? Just to suffer?"
    }

    @PostMapping("/update/{version}")
    fun update(@PathVariable version: String) : String {
        println("Received request to update to $version")
        return if (manager.updateDeploymentImage(newImage = "crickitycrick/cloud-app:${version.ifEmpty { "latest" }}")) "OK" else "FAIL"
    }
}
