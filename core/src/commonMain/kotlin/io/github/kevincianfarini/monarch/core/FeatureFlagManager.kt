package io.github.kevincianfarini.monarch.core

/**
 * Acquire values from [FeatureFlag] instances.
 */
public interface FeatureFlagManager {

    /**
     * Get the current value of [flag].
     */
    public fun <T : Any> currentValueOf(flag: FeatureFlag<T>): T
}