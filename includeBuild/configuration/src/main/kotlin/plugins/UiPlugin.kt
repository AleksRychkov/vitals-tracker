@file:Suppress("UnstableApiUsage")

package plugins

import App
import com.android.build.gradle.LibraryExtension
import dependencies.Deps
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

class UiPlugin : BasePlugin() {

    override fun applyPlugins(project: Project) {
        super.applyPlugins(project)
        project.plugins.apply {
            apply("com.android.library")
            apply("kotlin-android")
            apply("kotlin-kapt")
            apply("kotlin-parcelize")
        }
    }

    override fun applyExtension(project: Project) {
        (project.extensions.getByName("android") as? LibraryExtension)?.apply {
            compileSdk = App.compileSdkVersion

            defaultConfig {
                targetSdk = App.targetSdkVersion
                minSdk = App.minSdkVersion

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                consumerProguardFiles("proguard-rules.pro")
            }

            sourceSets {
                named("main") {
                    java {
                        srcDirs("src/main/kotlin")
                    }
                }
                named("androidTest") {
                    java {
                        srcDirs("src/androidTest/kotlin")
                    }
                }
                named("test") {
                    java {
                        srcDirs("src/test/kotlin")
                    }
                }
            }

            packagingOptions {
                resources.excludes.add("META-INF/AL2.0")
                resources.excludes.add("META-INF/LGPL2.1")
                resources.excludes.add("META-INF/*.kotlin_module")
            }
            testOptions {
                unitTests.all {
                    it.useJUnitPlatform()
                }
                unitTests.isReturnDefaultValues = true
            }

            buildFeatures.compose = true

            composeOptions {
                kotlinCompilerExtensionVersion = Deps.Compose.version
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
        }
        (project.extensions.getByName("kapt") as? KaptExtension)?.apply {
            correctErrorTypes = true
        }
    }

    override fun applyInternalDependencies(project: Project) {
        project.dependencies {
            add("implementation", project(":core:resources"))
            add("implementation", project(":core:navigation-contract"))

            add("testImplementation", project(":core:test"))
            add("testImplementation", testFixtures(project(":core:test")))
        }
    }

    override fun applyExternalDependencies(project: Project) {
        project.dependencies {
            // in order to make composes preview work we need to add this dependencies
            add("implementation", Deps.AndroidX.lifecycleRuntimeKtx)
            add("implementation", Deps.AndroidX.activityCompose)

            // compose
            add("debugImplementation", Deps.Compose.tooling)
            add("implementation", Deps.Compose.toolingPreview)
            add("implementation", Deps.Compose.runtime)
            add("implementation", Deps.Compose.ui)
            add("implementation", Deps.Compose.foundation)
            add("implementation", Deps.Compose.material)
            add("implementation", Deps.Compose.materialIconsExtended)

            // timber
            add("implementation", Deps.JakeWharton.timber)
        }
    }
}
