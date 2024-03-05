package io.github.kevincianfarini.monarch

import kotlinx.coroutines.flow.Flow

/**
 * Acquire a [Flow] of values from [FeatureFlag] instances.
 */
public interface ObservableFeatureFlagManager : FeatureFlagManager {

    /**
     * Return a [Flow] which emits when the value of [flag] changes.
     */
    public fun <T : Any> valuesOf(flag: FeatureFlag<T>): Flow<T>
}
