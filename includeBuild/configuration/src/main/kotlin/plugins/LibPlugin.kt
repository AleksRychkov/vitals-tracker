package plugins

import dependencies.Deps
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

class LibPlugin : BasePlugin() {
    override fun applyPlugins(project: Project) {
        super.applyPlugins(project)
        project.plugins.apply {
            apply("java-library")
            apply("kotlin")
            apply("kotlin-kapt")
        }
    }

    override fun applyExtension(project: Project) {
        (project.extensions.getByName("kapt") as? KaptExtension)?.apply {
            correctErrorTypes = true
        }
    }

    override fun applyExternalDependencies(project: Project) {
        project.dependencies {
            // AndroidX
            add("implementation", Deps.AndroidX.annotation)

            // testing
            add("testImplementation", Deps.Testing.jUnit5)
            add("testImplementation", Deps.Testing.mockk)
            add("testImplementation", Deps.KotlinX.coroutines)
        }
    }
}
