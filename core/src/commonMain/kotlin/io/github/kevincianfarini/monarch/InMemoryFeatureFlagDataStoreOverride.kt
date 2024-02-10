package io.github.kevincianfarini.monarch

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
public class InMemoryFeatureFlagDataStoreOverride(
    private val delegate: ObservableFeatureFlagDataStore,
    initialOverrides: Map<String, Any> = emptyMap()
) : ObservableFeatureFlagDataStore {

    private val cache: MutableStateFlow<Map<String, Any>> = MutableStateFlow(initialOverrides)

    public override fun getBoolean(key: String): Boolean? {
        return cache.getCachedValue<Boolean>(key) ?: delegate.getBoolean(key)
    }

    public override fun getString(key: String): String? {
        return cache.getCachedValue<String>(key) ?: delegate.getString(key)
    }

    public override fun getDouble(key: String): Double? {
        return cache.getCachedValue<Double>(key) ?: delegate.getDouble(key)
    }

    public override fun getLong(key: String): Long? {
        return cache.getCachedValue<Long>(key) ?: delegate.getLong(key)
    }

    public override fun getByteArray(key: String): ByteArray? {
        return cache.getCachedValue<ByteArray>(key) ?: delegate.getByteArray(key)
    }

    public override fun observeBoolean(key: String): Flow<Boolean?> {
        return cache.observeCachedValue<Boolean>(key).flatMapLatest { cachedValue ->
            when (cachedValue) {
                null -> delegate.observeBoolean(key)
                else -> flowOf(cachedValue)
            }
        }
    }

    public override fun observeString(key: String): Flow<String?> {
        return cache.observeCachedValue<String>(key).flatMapLatest { cachedValue ->
            when (cachedValue) {
                null -> delegate.observeString(key)
                else -> flowOf(cachedValue)
            }
        }
    }

    public override fun observeDouble(key: String): Flow<Double?> {
        return cache.observeCachedValue<Double>(key).flatMapLatest { cachedValue ->
            when (cachedValue) {
                null -> delegate.observeDouble(key)
                else -> flowOf(cachedValue)
            }
        }
    }

    public override fun observeLong(key: String): Flow<Long?> {
        return cache.observeCachedValue<Long>(key).flatMapLatest { cachedValue ->
            when (cachedValue) {
                null -> delegate.observeLong(key)
                else -> flowOf(cachedValue)
            }
        }
    }

    public override fun observeByteArray(key: String): Flow<ByteArray?> {
        return cache.observeCachedValue<ByteArray>(key).flatMapLatest { cachedValue ->
            when (cachedValue) {
                null -> delegate.observeByteArray(key)
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
    return value[key] as? T
}

private inline fun <reified T : Any> StateFlow<Map<String, Any>>.observeCachedValue(key: String): Flow<T?> {
    return map { map -> map[key] as? T }.distinctUntilChanged()
}
