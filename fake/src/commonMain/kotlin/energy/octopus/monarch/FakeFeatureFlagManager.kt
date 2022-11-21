package energy.octopus.monarch

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

public class FakeFeatureFlagManager : ObservableFeatureFlagManager {

    private val mutex = Mutex()
    private val store = mutableMapOf<String, MutableStateFlow<Any>>()

    @Suppress("UNCHECKED_CAST")
    public override suspend fun <T : Any> currentValueFor(
        flag: FeatureFlag<T>,
    ): T = mutex.withLock {
        (store[flag.key]?.value as? T) ?: flag.default
    }

    @Suppress("UNCHECKED_CAST")
    public override fun <T : Any> valuesFor(flag: FeatureFlag<T>): Flow<T> {
        return store.getOrPut(flag.key) { MutableStateFlow(flag.default) }.asStateFlow() as Flow<T>
    }

    public suspend fun <T : Any> setCurrentValueFor(
        flag: FeatureFlag<T>,
        option: T,
    ): Unit = mutex.withLock {
        if (store[flag.key] == null) {
            store[flag.key] = MutableStateFlow(option)
        } else {
            store[flag.key]?.value = option
        }
    }
}