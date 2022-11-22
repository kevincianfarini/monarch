package energy.octopus.monarch

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

public class FakeFeatureFlagDataStore : ObservableFeatureFlagDataStore {

    private val store = mutableMapOf<String, MutableStateFlow<Any?>>()
    private val mutex = Mutex()
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

    public override suspend fun getBoolean(key: String): Boolean? {
        return getValue(key) as Boolean?
    }

    public override suspend fun getString(key: String): String? {
        return getValue(key) as String?
    }

    public override suspend fun getDouble(key: String): Double? {
        return getValue(key) as Double?
    }

    public override suspend fun getLong(key: String): Long? {
        return getValue(key) as Long?
    }

    public override suspend fun getByteArray(key: String): ByteArray? {
        return getValue(key) as ByteArray?
    }

    public suspend fun setValue(key: String, value: Any?): Unit = mutex.withLock {
        if (store.containsKey(key)) {
            store[key]?.value = value
        } else {
            store[key] = MutableStateFlow(value)
        }
    }

    private fun getValues(key: String): MutableStateFlow<Any?> {
        return store.getOrPut(key) { MutableStateFlow(null) }
    }

    private suspend fun getValue(key: String): Any? = mutex.withLock {
        store[key]?.value
    }
}