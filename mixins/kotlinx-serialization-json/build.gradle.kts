plugins {
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.publish)
}

kotlin {
    explicitApi()

    iosArm64()
    iosSimulatorArm64()
    iosX64()
    jvm()
    js {
        nodejs {
            testTask {
                useMocha {
                    timeout = "5s"
                }
            }
        }
    }
    linuxArm64()
    linuxX64()
    macosArm64()
    macosX64()
    mingwX64()
    tvosArm64()
    tvosSimulatorArm64()
    tvosX64()
    watchosArm32()
    watchosArm64()
    watchosDeviceArm64()
    watchosSimulatorArm64()
    watchosX64()

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.serialization.json)
            api(project(":core"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(project(":test"))
        }
    }
}
