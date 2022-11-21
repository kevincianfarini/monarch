import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtensionConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

group = "energy.octopus"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}
