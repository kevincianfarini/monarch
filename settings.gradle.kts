pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

rootProject.name = "monarch"

include(":compose")
include(":core")
include(":integrations")
include(":integrations:environment-variable")
include(":integrations:launch-darkly")
include(":mixins")
include(":mixins:kotlinx-serialization-json")
include(":test")

