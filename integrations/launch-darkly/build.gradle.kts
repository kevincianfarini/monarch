import io.github.frankois944.spmForKmp.definition.SwiftDependency
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.publish)
    alias(libs.plugins.spmForKmp)
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

    iosArm64 { configureSpmInterop() }
    iosSimulatorArm64 { configureSpmInterop() }
    iosX64 { configureSpmInterop() }
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

private fun KotlinNativeTarget.configureSpmInterop() {
    compilations {
        val main by getting {
            cinterops.create("launchdarkly")
        }
    }
}

swiftPackageConfig {
    create("launchdarkly") {
        dependency(
            SwiftDependency.Package.Remote.Version(
                url = "https://github.com/launchdarkly/ios-client-sdk.git",
                names = listOf("LaunchDarkly"),
                packageName = "ios-client-sdk",
                version = "9.12.0",
                exportToKotlin = true,
            )
        )
    }
}