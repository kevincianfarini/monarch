package energy.octopus.featureflag

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

public class FakeFeatureFlagDataStore : FeatureFlagDataStore {

    private val store = mutableMapOf<String, Any?>()
    private val mutex = Mutex()

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
        store[key] = value
    }

    private suspend fun getValue(key: String): Any? = mutex.withLock {
        store[key]
    }
}