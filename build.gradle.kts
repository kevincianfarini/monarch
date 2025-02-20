import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.publish) apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

val jvmVersion: Provider<String> = providers.gradleProperty("kotlin.jvm.target")

subprojects {
    plugins.withType<com.android.build.gradle.BasePlugin>().configureEach {
        extensions.findByType<com.android.build.gradle.BaseExtension>()?.apply {
            jvmVersion.map { JavaVersion.toVersion(it) }.orNull?.let {
                compileOptions {
                    sourceCompatibility = it
                    targetCompatibility = it
                }
            }
        }
    }
    // Apply kotlinOptions.jvmTarget to subprojects
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            if (jvmVersion.isPresent) jvmTarget = jvmVersion.get()
        }
    }

    tasks.withType<AbstractTestTask> {
        testLogging {
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            showStandardStreams = true
            showStackTraces = true
            events(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        }
    }
}
