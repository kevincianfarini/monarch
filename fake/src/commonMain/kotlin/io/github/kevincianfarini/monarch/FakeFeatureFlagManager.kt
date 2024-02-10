package io.github.kevincianfarini.monarch

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

public class FakeFeatureFlagManager : ObservableFeatureFlagManager {

    private val store = mutableMapOf<String, MutableStateFlow<Any>>()

    @Suppress("UNCHECKED_CAST")
    public override fun <T : Any> currentValueFor(
        flag: FeatureFlag<T>,
    ): T = (store[flag.key]?.value as? T) ?: flag.default

    @Suppress("UNCHECKED_CAST")
    public override fun <T : Any> valuesFor(flag: FeatureFlag<T>): Flow<T> {
        return store.getOrPut(flag.key) { MutableStateFlow(flag.default) }.asStateFlow() as Flow<T>
    }

    public fun <T : Any> setCurrentValueFor(
        flag: FeatureFlag<T>,
        option: T,
    ) {
        if (store[flag.key] == null) {
            store[flag.key] = MutableStateFlow(option)
        } else {
            store[flag.key]?.value = option
        }
    }
}