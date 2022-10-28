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
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.turbine)
            }
        }
    }
}