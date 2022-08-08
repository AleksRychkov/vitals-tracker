package plugins


import dependencies.Deps
import dependencies.DepsPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

abstract class BasePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        applyPlugins(target)
        applyExtension(target)
        applyExternalDependencies(target)
        applyInternalDependencies(target)
        applyCommonDependencies(target)
    }


    open fun applyPlugins(project: Project) {
        project.plugins.apply {
            apply(DepsPlugin::class)
        }
    }

    private fun applyCommonDependencies(project: Project) {
        project.dependencies {
            add("implementation", Deps.Kotlin.stdLib)
        }
    }

    open fun applyExtension(project: Project) {}
    open fun applyExternalDependencies(project: Project) {}
    open fun applyInternalDependencies(project: Project) {}
}
