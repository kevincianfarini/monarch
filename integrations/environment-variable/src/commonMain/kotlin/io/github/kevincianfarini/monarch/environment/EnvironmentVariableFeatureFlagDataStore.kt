package io.github.kevincianfarini.monarch.environment

import io.github.kevincianfarini.monarch.FeatureFlagDataStore

/**
 * A [FeatureFlagDataStore] implementation that provides values from environment variables.
 */
public class EnvironmentVariableFeatureFlagDataStore internal constructor(
    private val strictlyTyped: Boolean = true,
    private val getEnvironmentVariable: (String) -> String?,
) : FeatureFlagDataStore {

    /**
     * Create an [EnvironmentVariableFeatureFlagDataStore] which reads from the environment.
     * If [strictlyTyped] is true, this store will throw exceptions when the raw string value
     * of the environment variable cannot be coerced to a specific type. Otherwise, this store
     * will return the default value.
     */
    public constructor(strictlyTyped: Boolean = true) : this(strictlyTyped, ::getSystemEnvVar)

    override fun getBoolean(key: String, default: Boolean): Boolean {
        val env = getEnvironmentVariable(key)
        val boolean = if (strictlyTyped) env?.toBooleanStrict() else env?.toBooleanStrictOrNull()
        return boolean ?: default
    }

    override fun getString(key: String, default: String): String {
        return getEnvironmentVariable(key) ?: default
    }

    override fun getDouble(key: String, default: Double): Double {
        val env = getEnvironmentVariable(key)
        val double = if (strictlyTyped) env?.toDouble() else env?.toDoubleOrNull()
        return double ?: default
    }

    override fun getLong(key: String, default: Long): Long {
        val env = getEnvironmentVariable(key)
        val long = if (strictlyTyped) env?.toLong() else env?.toLongOrNull()
        return long ?: default
    }

    override fun getByteArray(key: String, default: ByteArray): ByteArray {
        val env = getEnvironmentVariable(key)
        val bytes = if (strictlyTyped) env?.decodeHexToByteArray() else env?.decodeHexToByteArrayOrNull()
        return bytes ?: default
    }
}

private fun String.decodeHexToByteArrayOrNull(): ByteArray? = takeIf { it.length % 2 == 0 }?.let { string ->
    ByteArray(string.length / 2) { index ->
        val startIndex = index * 2
        val endIndex = startIndex + 1
        val byteString = string.substring(startIndex, endIndex + 1)
        byteString.toByte(16)
    }
}

private fun String.decodeHexToByteArray(): ByteArray {
    return requireNotNull(decodeHexToByteArrayOrNull()) {
        "The input string $this is not a valid hex string."
    }
}