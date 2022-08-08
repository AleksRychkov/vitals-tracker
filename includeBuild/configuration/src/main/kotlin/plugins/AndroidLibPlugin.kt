@file:Suppress("UnstableApiUsage")

package plugins

import App
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

class AndroidLibPlugin : BasePlugin() {
    override fun applyPlugins(project: Project) {
        super.applyPlugins(project)
        project.plugins.apply {
            apply("com.android.library")
            apply("kotlin-android")
            apply("kotlin-kapt")
        }
    }

    override fun applyExtension(project: Project) {
        (project.extensions.getByName("android") as? LibraryExtension)?.apply {
            compileSdk = App.compileSdkVersion

            defaultConfig {
                targetSdk = App.targetSdkVersion
                minSdk = App.minSdkVersion
                consumerProguardFiles("proguard-rules.pro")

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
                    excludes += "META-INF/LICENSE.md"
                    excludes += "META-INF/LICENSE-notice.md"
                }
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
        }
    }
}