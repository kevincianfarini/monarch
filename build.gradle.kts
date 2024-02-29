plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.publish) apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
