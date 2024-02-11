package io.github.kevincianfarini.monarch

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
public class InMemoryFeatureFlagDataStoreOverride(
    private val delegate: ObservableFeatureFlagDataStore,
    initialOverrides: Map<String, Any> = emptyMap()
) : ObservableFeatureFlagDataStore {

    private val cache: MutableStateFlow<Map<String, Any>> = MutableStateFlow(initialOverrides)

    public override fun getBoolean(key: String, default: Boolean): Boolean {
        return cache.getCachedValue<Boolean>(key) ?: delegate.getBoolean(key, default)
    }

    public override fun getString(key: String, default: String): String {
        return cache.getCachedValue<String>(key) ?: delegate.getString(key, default)
    }

    public override fun getDouble(key: String, default: Double): Double {
        return cache.getCachedValue<Double>(key) ?: delegate.getDouble(key, default)
    }

    public override fun getLong(key: String, default: Long): Long {
        return cache.getCachedValue<Long>(key) ?: delegate.getLong(key, default)
    }

    public override fun getByteArray(key: String, default: ByteArray): ByteArray {
        return cache.getCachedValue<ByteArray>(key) ?: delegate.getByteArray(key, default)
    }

    public override fun observeBoolean(key: String, default: Boolean): Flow<Boolean> {
        return cache.observeCachedValue<Boolean>(key).flatMapLatest { cachedValue ->
            when (cachedValue) {
                null -> delegate.observeBoolean(key, default)
                else -> flowOf(cachedValue)
            }
        }
    }

    public override fun observeString(key: String, default: String): Flow<String> {
        return cache.observeCachedValue<String>(key).flatMapLatest { cachedValue ->
            when (cachedValue) {
                null -> delegate.observeString(key, default)
                else -> flowOf(cachedValue)
            }
        }
    }

    public override fun observeDouble(key: String, default: Double): Flow<Double> {
        return cache.observeCachedValue<Double>(key).flatMapLatest { cachedValue ->
            when (cachedValue) {
                null -> delegate.observeDouble(key, default)
                else -> flowOf(cachedValue)
            }
        }
    }

    public override fun observeLong(key: String, default: Long): Flow<Long> {
        return cache.observeCachedValue<Long>(key).flatMapLatest { cachedValue ->
            when (cachedValue) {
                null -> delegate.observeLong(key, default)
                else -> flowOf(cachedValue)
            }
        }
    }

    public override fun observeByteArray(key: String, default: ByteArray): Flow<ByteArray> {
        return cache.observeCachedValue<ByteArray>(key).flatMapLatest { cachedValue ->
            when (cachedValue) {
                null -> delegate.observeByteArray(key, default)
                else -> flowOf(cachedValue)
            }
        }
    }

    public fun setBoolean(key: String, value: Boolean) {
        cache.update { map -> map + Pair(key, value) }
    }

    public fun setString(key: String, value: String) {
        cache.update { map -> map + Pair(key, value) }
    }

    public fun setDouble(key: String, value: Double) {
        cache.update { map -> map + Pair(key, value) }
    }

    public fun setLong(key: String, value: Long) {
        cache.update { map -> map + Pair(key, value) }
    }
    public fun setByteArray(key: String, value: ByteArray) {
        cache.update { map -> map + Pair(key, value) }
    }
}

private inline fun <reified T : Any> StateFlow<Map<String, Any>>.getCachedValue(key: String): T? {
    return value[key] as T?
}

private inline fun <reified T : Any> StateFlow<Map<String, Any>>.observeCachedValue(key: String): Flow<T?> {
    return map { map -> map[key] as T? }.distinctUntilChanged()
}
