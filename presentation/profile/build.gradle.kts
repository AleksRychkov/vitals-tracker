import dependencies.Deps

plugins {
    id("ui-plugin")
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:dependency"))

    implementation(project(":domain:common"))
    implementation(project(":domain:profile"))
}

dependencies {
    implementation(Deps.Accompanist.insets)

    implementation(Deps.AndroidX.viewModelKtx)
    implementation(Deps.AndroidX.viewModelSavedState)
    implementation(Deps.AndroidX.viewModelCompose)

    implementation(Deps.JakeWharton.threetenabp)
}