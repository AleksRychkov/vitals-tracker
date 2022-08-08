import dependencies.Deps

plugins {
    id("lib-plugin")
}

dependencies {
    compileOnly(Deps.Google.androidFramework)
}
