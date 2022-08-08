import dependencies.Deps

plugins {
    id("lib-plugin")
}

dependencies {
    implementation(Deps.KotlinX.coroutines)

    implementation(Deps.JakeWharton.threetenabp)

    testImplementation(Deps.Testing.threeten)
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":domain:common"))
}
