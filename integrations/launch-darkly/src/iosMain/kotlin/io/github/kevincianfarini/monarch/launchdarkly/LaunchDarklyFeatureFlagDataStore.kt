package io.github.kevincianfarini.monarch.launchdarkly

import io.github.kevincianfarini.monarch.ObservableFeatureFlagDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.pin
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

public fun LaunchDarklyClientShim.asFeatureFlagDataStore(): ObservableFeatureFlagDataStore {
    return LaunchDarklyFeatureFlagDataStore(this)
}

private class LaunchDarklyFeatureFlagDataStore(
    private val shim: LaunchDarklyClientShim
) : ObservableFeatureFlagDataStore {

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return shim.getValue(key, default)
    }

    override fun getString(key: String, default: String): String {
        return shim.getValue(key, default)
    }

    override fun getDouble(key: String, default: Double): Double {
        return shim.getValue(key, default)
    }

    override fun getLong(key: String, default: Long): Long {
        return shim.getValue(key, default)
    }

    override fun getByteArray(key: String, default: ByteArray): ByteArray {
        throw NotImplementedError("LaunchDarkly does not support ByteArray flags.")
    }

    override fun observeString(key: String, default: String): Flow<String> {
        return shim.observeValue(key, default)
    }

    override fun observeBoolean(key: String, default: Boolean): Flow<Boolean> {
        return shim.observeValue(key, default)
    }

    override fun observeDouble(key: String, default: Double): Flow<Double> {
        return shim.observeValue(key, default)
    }

    override fun observeLong(key: String, default: Long): Flow<Long> {
        return shim.observeValue(key, default)
    }

    override fun observeByteArray(key: String, default: ByteArray): Flow<ByteArray> {
        throw NotImplementedError("LaunchDarkly does not support ByteArray flags.")
    }
}

@OptIn(ExperimentalForeignApi::class)
private inline fun <reified T : Any> LaunchDarklyClientShim.observeValue(key: String, default: T): Flow<T> {
    return callbackFlow {
        trySend(getValue<T>(key, default)).getOrThrow()
        val owner = Any().pin()
        observe(key, owner.get()) { trySend(getValue<T>(key, default)).getOrThrow() }
        awaitClose {
            stopObserving(owner.get())
            owner.unpin()
        }
    }.conflate()
}

private inline fun <reified T : Any> LaunchDarklyClientShim.getValue(key: String, default: T): T {
    return when (val clazz = T::class) {
        Boolean::class -> boolVariation(key, default as Boolean) as T
        String::class -> {
            val jsonString = jsonStringVariation(key, null)
            when (jsonString) {
                null -> stringVariation(key, default as String)
                else -> jsonString
            } as T
        }
        Double::class -> doubleVariation(key, default as Double) as T
        Long::class -> intVariation(key, (default as Long).toInt()).toLong() as T
        else -> throw IllegalArgumentException("Illegal type for getValue: $clazz")
    }
}
