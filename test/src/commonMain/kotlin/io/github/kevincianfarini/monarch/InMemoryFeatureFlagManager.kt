package io.github.kevincianfarini.monarch

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * An implementation of [ObservableFeatureFlagManager] that allows mutating the value of flags in-memory.
 */
public class InMemoryFeatureFlagManager : ObservableFeatureFlagManager {

    private val store = MutableStateFlow<Map<String, Any>>(emptyMap())

    @Suppress("UNCHECKED_CAST")
    public override fun <T : Any> currentValueOf(flag: FeatureFlag<T>): T {
        return (store.value[flag.key] ?: flag.default) as T
    }

    @Suppress("UNCHECKED_CAST")
    public override fun <T : Any> valuesOf(flag: FeatureFlag<T>): Flow<T> {
        return store.map { map -> (map[flag.key] ?: flag.default) as T }
    }

    /**
     * Set the value of [flag] in this manager instance to [option].
     */
    public fun <T : Any> setCurrentValueOf(flag: FeatureFlag<T>, option: T) {
        store.update { map -> map.plus(flag.key to option) }
    }
}