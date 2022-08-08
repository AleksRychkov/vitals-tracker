plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.1.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
}

kotlin {
    sourceSets {
        named("main") {
            kotlin.apply {
                srcDir("configuration/src/main/kotlin")
            }
        }
    }
}

gradlePlugin {
    plugins.register("app-plugin") {
        id = "app-plugin"
        implementationClass = "plugins.AppPlugin"
    }
    plugins.register("lib-plugin") {
        id = "lib-plugin"
        implementationClass = "plugins.LibPlugin"
    }
    plugins.register("android-lib-plugin") {
        id = "android-lib-plugin"
        implementationClass = "plugins.AndroidLibPlugin"
    }
    plugins.register("ui-plugin") {
        id = "ui-plugin"
        implementationClass = "plugins.UiPlugin"
    }
}
