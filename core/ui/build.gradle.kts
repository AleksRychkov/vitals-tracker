import dependencies.Deps

plugins {
    id("ui-plugin")
}

dependencies {
    implementation(Deps.Accompanist.insets)
    implementation(Deps.Accompanist.pager)

    implementation(Deps.JakeWharton.threetenabp)

    implementation(Deps.AndroidX.viewModelKtx)
    implementation(Deps.AndroidX.viewModelSavedState)
}

dependencies {
    implementation(project(":core:sysservice"))
    implementation(project(":core:common"))

    implementation(project(":domain:common"))
}
