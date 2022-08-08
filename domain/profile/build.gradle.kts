import dependencies.Deps

plugins {
    id("lib-plugin")
}

dependencies {
    implementation(Deps.KotlinX.coroutines)

    implementation(project(":domain:common"))

    api(project(":core:common"))
}