import org.apache.tools.ant.taskdefs.condition.Os

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
include(":mixins")
include(":mixins:kotlinx-serialization-json")
include(":test")

// The SPM4KMP plugin only works on macOS hosts. Optionally include this for now.
if (Os.isFamily(Os.FAMILY_MAC)) {
    include(":integrations:launch-darkly")
}

