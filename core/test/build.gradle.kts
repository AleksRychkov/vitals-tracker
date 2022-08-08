import dependencies.Deps

plugins {
    id("lib-plugin")
    id("java-test-fixtures")
}

dependencies {
    api(Deps.Testing.jUnit5)
    api(Deps.Testing.mockk)
    api(Deps.KotlinX.coroutines)
    api(Deps.Testing.threeten)
    api(Deps.Testing.turbine)
    api(Deps.Testing.kotlinCoroutinesTest)
    api(Deps.Testing.archCore)
}