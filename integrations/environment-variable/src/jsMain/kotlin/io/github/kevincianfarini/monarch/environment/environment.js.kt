package io.github.kevincianfarini.monarch.environment

import node.process.process

internal actual fun getSystemEnvVar(key: String): String? {
    return process.env[key]
}