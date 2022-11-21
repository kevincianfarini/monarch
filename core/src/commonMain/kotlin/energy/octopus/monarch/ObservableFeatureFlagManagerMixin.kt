package energy.octopus.monarch

import kotlinx.coroutines.flow.Flow

/**
 * A supplement to [ObservableMixinFeatureFlagManager] that allows extension via
 * [ObservableFeatureFlagManagerMixin.valuesOrNull]. Implementations of this interface
 * can opt to handle 1 or more [FeatureFlag] types. For example, if we want to add a mixin that
 * supports JSON, we could do so with the following code.
 *
 * ```kt
 * class JsonFeatureFlagMixin : ObservableFeatureFlagMixin {
 *
 *   override suspend fun <T : FeatureFlagOption> valuesOrNull(
 *     flag: FeatureFlag<T>,
 *     store: ObservableFeatureFlagDataStore,
 *   ): T? = when (flag) {
 *     is SomeJsonFeatureFlag<T> -> store.stringFlow(flag.key).map { decodeJson(it) }
 *     else -> null
 *   }
 *
 *   abstract fun <T : FeatureFlagOption> decodeJson(flag: FeatureFlag<T>): T
 * }
 * ```
 */
public interface ObservableFeatureFlagManagerMixin: FeatureFlagManagerMixin {

    /**
     * Observes values for [flag] as [Flow<T>] from [store]. This function will return null if this
     * [ObservableFeatureFlagManagerMixin] does not handle [flag].
     */
    public fun <T : Any> valuesOrNull(
        flag: FeatureFlag<T>,
        store: ObservableFeatureFlagDataStore,
    ): Flow<T>?
}