plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {

    explicitApi()
    jvm()
    ios()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core"))
                api(libs.kotlinx.coroutines.core)
            }
        }
    }
}
