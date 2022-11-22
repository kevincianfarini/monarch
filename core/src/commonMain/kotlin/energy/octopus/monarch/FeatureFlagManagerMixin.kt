package energy.octopus.monarch

/**
 * A supplement to [MixinFeatureFlagManager] that allows extension via
 * [FeatureFlagManagerMixin.currentValueForOrNull]. Implementations of this interface
 * can opt to handle 1 or more [FeatureFlag] types. For example, if we want to add a mixin that
 * supports JSON, we could do so with the following code.
 *
 * ```kt
 * class JsonFeatureFlagMixin : FeatureFlagMixin {
 *
 *   override suspend fun <T : FeatureFlagOption> currentValueForOrNull(
 *     flag: FeatureFlag<T>,
 *     store: FeatureFlagDataStore,
 *   ): T? = when (flag) {
 *     is SomeJsonFeatureFlag<T> -> decodeJson(store.getString(flag.key))
 *     else -> null
 *   }
 *
 *   abstract suspend fun <T : FeatureFlagOption> decodeJson(flag: FeatureFlag<T>): T
 * }
 * ```
 */
public interface FeatureFlagManagerMixin {

    /**
     * Get the current value for [flag] as [T] from [store]. This function will return null if this
     * [FeatureFlagManagerMixin] does not handle [flag].
     */
    public fun <T : Any> currentValueForOrNull(
        flag: FeatureFlag<T>,
        store: FeatureFlagDataStore,
    ): T?
}