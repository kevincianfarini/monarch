plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.publish)
}

android {
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    namespace = "io.github.kevincianfarini.monarch.launchdarkly"
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    buildFeatures {
        buildConfig = false
    }
}

kotlin {
    explicitApi()
    jvmToolchain(17)

    iosArm64()
    iosSimulatorArm64()
    iosX64()
    androidTarget {
        publishLibraryVariants("release")
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":core"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
        }
        androidMain.dependencies {
            api(libs.launchdarkly.android)
        }
    }
}