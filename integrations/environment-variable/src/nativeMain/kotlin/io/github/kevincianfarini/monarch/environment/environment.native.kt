package io.github.kevincianfarini.monarch.environment

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

@OptIn(ExperimentalForeignApi::class)
internal actual fun getSystemEnvVar(key: String): String? {
    return getenv(key)?.toKString()
}