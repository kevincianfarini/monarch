package io.github.kevincianfarini.monarch

import kotlinx.coroutines.flow.Flow

/**
 * A [ObservableFeatureFlagManager] implementation that allows extension via [mixins].
 */
public class ObservableMixinFeatureFlagManager(
    /**
     * The datastore which contains raw feature flag data.
     */
    private val store: ObservableFeatureFlagDataStore,

    /**
     * A list of [FeatureFlagManagerMixin] that can be queried when this manager is unfit to handle the
     * feature flag query.
     */
    private val mixins: List<ObservableFeatureFlagManagerMixin> = emptyList(),
) : ObservableFeatureFlagManager, FeatureFlagManager by MixinFeatureFlagManager(store, mixins) {

    @Suppress("UNCHECKED_CAST")
    public override fun <T : Any> valuesOf(flag: FeatureFlag<T>): Flow<T> = when (flag) {
        is BooleanFeatureFlag -> store.observeBoolean(flag.key, flag.default) as Flow<T>
        is StringFeatureFlag -> store.observeString(flag.key, flag.default) as Flow<T>
        is DoubleFeatureFlag -> store.observeDouble(flag.key, flag.default) as Flow<T>
        is LongFeatureFlag -> store.observeLong(flag.key, flag.default) as Flow<T>
        else -> mixins.firstNotNullOfOrNull { delegate ->
            delegate.valuesOfOrNull(flag, store)
        } ?: throw IllegalArgumentException("$flag is not a recognized feature flag.")
    }
}