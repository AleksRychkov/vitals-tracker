pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
    }
}

rootProject.name = "vitals-tracker"

includeBuild("includeBuild/configuration")

include(":app")

// domain
include(":domain:common")
include(":domain:measurements")
include(":domain:profile")
include(":domain:dashboard")

// data
include(":data")
include(":database")

// core
include(":core:common")
include(":core:dependency")
include(":core:resources")
include(":core:ui")
include(":core:navigation")
include(":core:navigation-contract")
include(":core:sysservice")
include(":core:test")

// presentation
include(":presentation:splash")
include(":presentation:profile")
include(":presentation:dashboard")
include(":presentation:measurements")
