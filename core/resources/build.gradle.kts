import dependencies.Deps

plugins {
    id("android-lib-plugin")
}

dependencies {
    implementation(Deps.AndroidX.annotation)
}

dependencies {
    implementation(project(":domain:common"))
}