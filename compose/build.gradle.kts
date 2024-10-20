plugins {
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.publish)
}

kotlin {

    explicitApi()

    iosArm64()
    iosSimulatorArm64()
    iosX64()
    jvm()
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
    watchosSimulatorArm64()
    watchosX64()

    sourceSets {
        commonMain.dependencies {
            api(libs.compose.runtime)
            api(project(":core"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.molecule)
            implementation(libs.turbine)
            implementation(project(":test"))
        }
    }
}