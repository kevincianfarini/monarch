package io.github.kevincianfarini.monarch.environment

internal actual fun getSystemEnvVar(key: String): String? {
    return System.getenv(key)
}