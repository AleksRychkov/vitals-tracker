import dependencies.Deps

plugins {
    id("android-lib-plugin")
}

dependencies {
    implementation(Deps.KotlinX.coroutines)

    implementation(Deps.BinaryPrefs.binaryprefs)

    implementation(Deps.JakeWharton.threetenabp)

    testImplementation(Deps.Testing.jUnit5)
    testImplementation(Deps.Testing.mockk)
}

dependencies {
    implementation(project(":database"))

    implementation(project(":domain:common"))
    implementation(project(":domain:measurements"))
    implementation(project(":domain:profile"))
    implementation(project(":domain:dashboard"))
}