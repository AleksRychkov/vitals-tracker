@file:Suppress("UnstableApiUsage")

package plugins

import App
import com.android.build.gradle.AppExtension
import dependencies.Deps
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.project
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

class AppPlugin : BasePlugin() {

    override fun applyPlugins(project: Project) {
        super.applyPlugins(project)
        project.plugins.apply {
            apply("com.android.application")
            apply("kotlin-android")
            apply("kotlin-kapt")
        }
    }

    override fun applyExtension(project: Project) {
        (project.extensions.getByName("android") as? AppExtension)?.apply {
            compileSdkVersion(App.compileSdkVersion)

            defaultConfig {
                applicationId = App.id

                versionCode(App.versionCode)
                versionName(App.versionName)

                targetSdk = App.targetSdkVersion
                minSdk = App.minSdkVersion

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                named("debug") {
                    applicationIdSuffix = ".debug"
                    versionNameSuffix = "-debug"

                    isMinifyEnabled = false
                    isTestCoverageEnabled = true

                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }

                named("release") {
                    isDebuggable = false
                    isMinifyEnabled = true
                    isShrinkResources = true

                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            buildFeatures.compose = true

            composeOptions {
                kotlinCompilerExtensionVersion = Deps.Compose.version
            }

            sourceSets {
                named("main") {
                    java {
                        srcDirs("src/main/kotlin")
                    }
                }
            }
            packagingOptions {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    excludes += "META-INF/*.kotlin_module"
                }
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
            add("implementation", project(":core:ui"))
            add("implementation", project(":core:common"))
            add("implementation", project(":core:dependency"))
            add("implementation", project(":core:navigation"))
            add("implementation", project(":core:navigation-contract"))
            add("implementation", project(":core:sysservice"))
        }
    }

    override fun applyExternalDependencies(project: Project) {
        project.dependencies {
            // AndroidX
            add("implementation", Deps.AndroidX.activityCompose)
            add("implementation", Deps.AndroidX.navigationCompose)

            // compose
            add("implementation", Deps.Compose.foundation)
            add("implementation", Deps.Compose.ui)
            add("implementation", Deps.Compose.material)
            add("implementation", Deps.Compose.toolingPreview)
            add("debugImplementation", Deps.Compose.tooling)

            // accompanist
            add("implementation", Deps.Accompanist.insets)
            add("implementation", Deps.Accompanist.systemUiController)

            // threetenabp
            add("implementation", Deps.JakeWharton.threetenabp)

            // timber
            add("implementation", Deps.JakeWharton.timber)
        }
    }
}
