import dependencies.Deps

plugins {
    id("ui-plugin")
}

dependencies {
    implementation(Deps.AndroidX.navigationCompose)
    implementation(Deps.Compose.runtime)
}

dependencies {
    implementation(project(":core:navigation-contract"))

    implementation(project(":presentation:splash"))
    implementation(project(":presentation:profile"))
    implementation(project(":presentation:dashboard"))
    implementation(project(":presentation:measurements"))
}
