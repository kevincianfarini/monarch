package io.github.kevincianfarini.monarch

/**
 * A supplement to [MixinFeatureFlagManager] that allows extension via [FeatureFlagManagerMixin.currentValueOfOrNull].
 *
 * Implementations of this interface can opt to handle 1 or more [FeatureFlag] types. For example, if we want to add a
 * mixin that supports JSON, we could do so with the following code.
 *
 * ```kt
 * class JsonFeatureFlagMixin : FeatureFlagMixin {
 *
 *   override fun <T : FeatureFlagOption> currentValueOfOrNull(
 *     flag: FeatureFlag<T>,
 *     store: FeatureFlagDataStore,
 *   ): T? = when (flag) {
 *     is SomeJsonFeatureFlag<T> -> decodeJson(store.getString(flag.key))
 *     else -> null
 *   }
 *
 *   abstract fun <T : FeatureFlagOption> decodeJson(flag: FeatureFlag<T>): T
 * }
 * ```
 */
public interface FeatureFlagManagerMixin {

    /**
     * Get the current value for [flag] as [T] from [store]. This function will return null if this
     * [FeatureFlagManagerMixin] does not handle [flag].
     */
    public fun <T : Any> currentValueOfOrNull(
        flag: FeatureFlag<T>,
        store: FeatureFlagDataStore,
    ): T?
}