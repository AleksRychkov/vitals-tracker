import dependencies.Deps

plugins {
    id("android-lib-plugin")
}

android {
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }
}

dependencies {
    implementation(Deps.KotlinX.coroutines)

    api(Deps.Room.room)
    implementation(Deps.Room.roomKtx)
    kapt(Deps.Room.roomCompiler)

    androidTestImplementation(Deps.Testing.jUnitKtx)
    androidTestImplementation(Deps.Testing.room)
    androidTestImplementation(Deps.Testing.archCore)
    androidTestImplementation(Deps.Testing.androidXRunner)
    androidTestImplementation(Deps.Testing.androidXCore)
}
