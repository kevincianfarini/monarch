import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtensionConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}
