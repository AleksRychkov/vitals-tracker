import dependencies.Deps

plugins {
    id("android-lib-plugin")
}

dependencies {
    implementation(Deps.AndroidX.lifecycleCommon)
}

dependencies {
    implementation(project(":core:sysservice"))
    implementation(project(":core:navigation-contract"))

    implementation(project(":domain:measurements"))
    implementation(project(":domain:profile"))
    implementation(project(":domain:dashboard"))

    implementation(project(":data"))
}