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
include(":integrations:environment-variable")
include(":mixins:kotlinx-serialization-json")
include(":test")

// The SPM4KMP plugin only works on macOS hosts. Optionally include this for now until the plugin
// is able to no-op on non-macOS build hosts.
if (Os.isFamily(Os.FAMILY_MAC)) {
    include(":integrations:launch-darkly")
}

