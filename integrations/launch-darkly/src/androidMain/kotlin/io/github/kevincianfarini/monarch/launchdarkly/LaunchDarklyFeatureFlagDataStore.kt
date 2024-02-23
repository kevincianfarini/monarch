package io.github.kevincianfarini.monarch.launchdarkly

import com.launchdarkly.sdk.android.LDClientInterface
import io.github.kevincianfarini.monarch.ObservableFeatureFlagDataStore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

public fun LDClientInterface.asFeatureFlagDataStore(): ObservableFeatureFlagDataStore {
    return LaunchDarklyFeatureFlagDataStore(this)
}

private class LaunchDarklyFeatureFlagDataStore(
    private val client: LDClientInterface
) : ObservableFeatureFlagDataStore {

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return client.getValue(key, default)
    }

    override fun getString(key: String, default: String): String {
        return client.getValue(key, default)
    }

    override fun getDouble(key: String, default: Double): Double {
        return client.getValue(key, default)
    }

    override fun getLong(key: String, default: Long): Long {
        return client.getValue(key, default)
    }

    override fun getByteArray(key: String, default: ByteArray): ByteArray {
        throw NotImplementedError("LaunchDarkly does not support ByteArray flags.")
    }

    override fun observeString(key: String, default: String): Flow<String> = client.observeValue(key, default)

    override fun observeBoolean(key: String, default: Boolean): Flow<Boolean> = client.observeValue(key, default)

    override fun observeDouble(key: String, default: Double): Flow<Double> = client.observeValue(key, default)

    override fun observeLong(key: String, default: Long): Flow<Long> = client.observeValue(key, default)

    override fun observeByteArray(key: String, default: ByteArray): Flow<ByteArray> {
        throw NotImplementedError("LaunchDarkly does not support ByteArray flags.")
    }
}

private inline fun <reified T : Any> LDClientInterface.observeValue(key: String, default: T): Flow<T> {
    return callbackFlow {
        trySend(getValue<T>(key, default)).getOrThrow()
        val callback = { key: String -> trySend(getValue<T>(key, default)).getOrThrow() }
        registerFeatureFlagListener(key, callback)
        awaitClose { unregisterFeatureFlagListener(key, callback) }
    }.conflate()
}

private inline fun <reified T : Any> LDClientInterface.getValue(key: String, default: T): T {
    return when (val clazz = T::class) {
        Boolean::class -> boolVariation(key, default as Boolean) as T
        String::class -> stringVariation(key, default as String) as T
        Double::class -> doubleVariation(key, default as Double) as T
        Long::class -> intVariation(key, (default as Long).toInt()).toLong() as T
        else -> throw IllegalArgumentException("Illegal type for getValue: $clazz")
    }
}

