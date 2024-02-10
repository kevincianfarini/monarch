package io.github.kevincianfarini.monarch

/**
 * Manage the options a [FeatureFlag] can manifest as.
 */
public interface FeatureFlagManager {

    /**
     * Get the current value of [flag].
     */
    public fun <T : Any> currentValueFor(flag: FeatureFlag<T>): T
}
