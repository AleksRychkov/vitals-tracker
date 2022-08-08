package dependencies

object Deps {

    object Kotlin {
        private const val version = "1.6.10"
        val stdLib: String
            get() = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
    }

    object AndroidX {
        private const val annotationVersion = "1.3.0"
        val annotation: String
            get() = "androidx.annotation:annotation:$annotationVersion"

        private const val lifecycleVersion = "2.4.1"
        val lifecycleCommon: String
            get() = "androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion"
        val lifecycleRuntimeKtx: String
            get() = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion"
        val viewModelKtx: String
            get() = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
        val viewModelSavedState: String
            get() = "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion"
        val viewModelCompose: String
            get() = "androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion"

        private val activityComposeVersion = "1.4.0"
        val activityCompose: String
            get() = "androidx.activity:activity-compose:$activityComposeVersion"

        private val navigationComposeComposeVersion = "2.4.0"
        val navigationCompose: String
            get() = "androidx.navigation:navigation-compose:$navigationComposeComposeVersion"

    }

    object KotlinX {
        private const val coroutinesVersion = "1.6.0"

        val coroutines: String
            get() = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"

        val coroutinesAndroid: String
            get() = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"

        val coroutinesTest: String
            get() = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion"
    }

    object Google {
        private const val frameworkVersion = "4.1.1.4"
        val androidFramework: String
            get() = "com.google.android:android:$frameworkVersion"
    }

    object Compose {
        const val version: String = "1.1.1"

        val runtime: String
            get() = "androidx.compose.runtime:runtime:$version"
        val foundation: String
            get() = "androidx.compose.foundation:foundation:$version"
        val layout: String
            get() = "androidx.compose.foundation:foundation-layout:$version"
        val material: String
            get() = "androidx.compose.material:material:$version"
        val materialIconsExtended: String
            get() = "androidx.compose.material:material-icons-extended:$version"
        val ui: String
            get() = "androidx.compose.ui:ui:$version"
        val tooling: String
            get() = "androidx.compose.ui:ui-tooling:$version"
        val toolingPreview: String
            get() = "androidx.compose.ui:ui-tooling-preview:$version"
    }

    object Accompanist {
        private const val version = "0.23.1"
        val insets: String
            get() = "com.google.accompanist:accompanist-insets:$version"
        val insetsUi: String
            get() = "com.google.accompanist:accompanist-insets-ui:$version"
        val systemUiController: String
            get() = "com.google.accompanist:accompanist-systemuicontroller:$version"
        val pager: String
            get() = "com.google.accompanist:accompanist-pager:$version"
        val flowlayout: String
            get() = "com.google.accompanist:accompanist-flowlayout:$version"
    }

    object JakeWharton {
        private const val threetenabpVersion = "1.3.1"
        val threetenabp: String
            get() = "com.jakewharton.threetenabp:threetenabp:$threetenabpVersion"

        private const val timberVersion = "5.0.1"
        val timber: String
            get() = "com.jakewharton.timber:timber:$timberVersion"
    }

    private const val roomVersion = "2.4.2"

    object Room {
        val room: String
            get() = "androidx.room:room-runtime:$roomVersion"
        val roomKtx: String
            get() = "androidx.room:room-ktx:$roomVersion"
        val roomCompiler: String
            get() = "androidx.room:room-compiler:$roomVersion"
    }

    object BinaryPrefs {
        private const val version = "1.0.1"
        val binaryprefs: String
            get() = "com.github.yandextaxitech:binaryprefs:$version"
    }

    object Testing {
        private const val jUnit4Version = "4.12"
        private const val jUnit5Version = "5.3.2"
        private const val jUnitKtVersion = "1.1.3"
        private const val androidXVersion = "1.4.0"
        private const val jsonVersion = "20180813"
        private const val mockkVersion = "1.12.0"
        private const val threetenVersion = "1.5.1"
        private const val turbineVersion = "0.7.0"
        private const val archCoreVersion = "2.1.0"
        private const val kotlinCoroutinesTestVersion = "1.6.0"

        val jUnit4: String
            get() = "org.junit.jupiter:junit-jupiter-engine:$jUnit4Version"
        val jUnit5: String
            get() = "org.junit.jupiter:junit-jupiter-engine:$jUnit5Version"
        val jUnitKtx: String
            get() = "androidx.test.ext:junit-ktx:$jUnitKtVersion"
        val androidXRunner: String
            get() = "androidx.test:runner:$androidXVersion"
        val androidXCore: String
            get() = "androidx.test:core:$androidXVersion"
        val json: String
            get() = "org.json:json:$jsonVersion"
        val mockk: String
            get() = "io.mockk:mockk:$mockkVersion"
        val mockkAndroid: String
            get() = "io.mockk:mockk-android:$mockkVersion"
        val threeten: String
            get() = "org.threeten:threetenbp:$threetenVersion"
        val turbine: String
            get() = "app.cash.turbine:turbine:$turbineVersion"
        val room: String
            get() = "androidx.room:room-testing:$roomVersion"
        val archCore: String
            get() = "androidx.arch.core:core-testing:$archCoreVersion"
        val kotlinCoroutinesTest: String
            get() = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesTestVersion"
    }
}