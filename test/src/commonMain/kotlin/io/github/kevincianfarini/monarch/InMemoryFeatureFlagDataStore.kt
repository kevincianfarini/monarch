package io.github.kevincianfarini.monarch

import kotlinx.coroutines.flow.*

public class InMemoryFeatureFlagDataStore : ObservableFeatureFlagDataStore {

    private val store = MutableStateFlow<Map<String, Any?>>(emptyMap())

    public override fun observeString(key: String, default: String): Flow<String> {
        return store.observeValue(key, default)
    }

    public override fun observeBoolean(key: String, default: Boolean): Flow<Boolean> {
        return store.observeValue(key, default)
    }

    public override fun observeDouble(key: String, default: Double): Flow<Double> {
        return store.observeValue(key, default)
    }

    public override fun observeLong(key: String, default: Long): Flow<Long> {
        return store.observeValue(key, default)
    }

    public override fun observeByteArray(key: String, default: ByteArray): Flow<ByteArray> {
        return store.observeValue(key, default)
    }

    public override fun getBoolean(key: String, default: Boolean): Boolean {
        return store.getValue(key, default)
    }

    public override fun getString(key: String, default: String): String {
        return store.getValue(key, default)
    }

    public override fun getDouble(key: String, default: Double): Double {
        return store.getValue(key, default)
    }

    public override fun getLong(key: String, default: Long): Long {
        return store.getValue(key, default)
    }

    public override fun getByteArray(key: String, default: ByteArray): ByteArray {
        return store.getValue(key, default)
    }

    public fun setValue(key: String, value: Any?) {
        store.update { map ->
            map.plus(key to value)
        }
    }
}

private inline fun <reified T> StateFlow<Map<String, Any?>>.getValue(key: String, default: T): T {
    return (value[key] as T?) ?: default
}

private inline fun <reified T> StateFlow<Map<String, Any?>>.observeValue(key: String, default: T): Flow<T> {
    return map { map -> (map[key] as T?) ?: default }.distinctUntilChanged()
}
