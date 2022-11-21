plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    explicitApi()
    jvm()
    ios()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlinx.coroutines.core)
                api(libs.kotlinx.serialization.json)
                api(project(":core"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(project(":fake"))
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
