import dependencies.Deps

plugins {
    id("ui-plugin")
}

dependencies {
    implementation(project(":core:dependency"))

    implementation(project(":domain:profile"))
}