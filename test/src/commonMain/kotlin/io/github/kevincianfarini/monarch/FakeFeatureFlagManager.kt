package io.github.kevincianfarini.monarch

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

public class FakeFeatureFlagManager : ObservableFeatureFlagManager {

    private val store = MutableStateFlow<Map<String, Any>>(emptyMap())

    @Suppress("UNCHECKED_CAST")
    public override fun <T : Any> currentValueFor(flag: FeatureFlag<T>): T {
        return (store.value[flag.key] ?: flag.default) as T
    }

    @Suppress("UNCHECKED_CAST")
    public override fun <T : Any> valuesFor(flag: FeatureFlag<T>): Flow<T> {
        return store.map { map -> (map[flag.key] ?: flag.default) as T }
    }

    public fun <T : Any> setCurrentValueFor(flag: FeatureFlag<T>, option: T) {
        store.update { map -> map.plus(flag.key to option) }
    }
}