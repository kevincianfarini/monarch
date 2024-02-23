plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.plugin)
}

kotlin {

    explicitApi()

    iosArm64()
    iosSimulatorArm64()
    iosX64()
    jvm()
    linuxX64()
    macosArm64()
    macosX64()
    mingwX64()
    tvosArm64()
    tvosSimulatorArm64()
    tvosX64()
    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()
    watchosX64()

    sourceSets {
        commonMain.dependencies {
            api(libs.compose.runtime)
            api(project(":core"))
        }
    }
}