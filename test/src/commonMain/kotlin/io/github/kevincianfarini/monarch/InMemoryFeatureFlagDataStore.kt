package io.github.kevincianfarini.monarch

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

public class InMemoryFeatureFlagDataStore : ObservableFeatureFlagDataStore {

    private val store = MutableStateFlow<Map<String, Any?>>(emptyMap())

    public override fun observeString(key: String): Flow<String?> {
        return getValues(key).map { it as String? }
    }

    public override fun observeBoolean(key: String): Flow<Boolean?> {
        return getValues(key).map { it as Boolean? }
    }

    public override fun observeDouble(key: String): Flow<Double?> {
        return getValues(key).map { it as Double? }
    }

    public override fun observeLong(key: String): Flow<Long?> {
        return getValues(key).map { it as Long? }
    }

    public override fun observeByteArray(key: String): Flow<ByteArray?> {
        return getValues(key).map { it as ByteArray? }
    }

    public override fun getBoolean(key: String): Boolean? {
        return getValue(key) as Boolean?
    }

    public override fun getString(key: String): String? {
        return getValue(key) as String?
    }

    public override fun getDouble(key: String): Double? {
        return getValue(key) as Double?
    }

    public override fun getLong(key: String): Long? {
        return getValue(key) as Long?
    }

    public override fun getByteArray(key: String): ByteArray? {
        return getValue(key) as ByteArray?
    }

    public fun setValue(key: String, value: Any?) {
        store.update { map ->
            map.plus(key to value)
        }
    }

    private fun getValues(key: String): Flow<Any?> {
        return store.map { map -> map[key] }
    }

    private fun getValue(key: String): Any? = store.value[key]
}