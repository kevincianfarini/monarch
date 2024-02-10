package io.github.kevincianfarini.monarch

import kotlinx.coroutines.flow.Flow

public interface ObservableFeatureFlagManager : FeatureFlagManager {

    /**
     * Emits value changes of [flag].
     */
    public fun <T : Any> valuesOf(flag: FeatureFlag<T>): Flow<T>
}
