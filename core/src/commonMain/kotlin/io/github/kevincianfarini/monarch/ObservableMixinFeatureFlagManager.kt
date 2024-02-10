package io.github.kevincianfarini.monarch

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
    public override fun <T : Any> valuesFor(flag: FeatureFlag<T>): Flow<T> = when (flag) {
        is BooleanFeatureFlag -> store.observeBoolean(flag.key).map { value ->
            (value ?: flag.default) as T
        }
        is StringFeatureFlag -> store.observeString(flag.key).map { value ->
            (value ?: flag.default) as T
        }
        is DoubleFeatureFlag -> store.observeDouble(flag.key).map { value ->
            (value ?: flag.default) as T
        }
        is LongFeatureFlag -> store.observeLong(flag.key).map { value ->
            (value ?: flag.default) as T
        }
        is ByteArrayFeatureFlag -> store.observeByteArray(flag.key).map { value ->
            (value ?: flag.default) as T
        }
        else -> mixins.firstNotNullOfOrNull { delegate ->
            delegate.valuesOrNull(flag, store)
        } ?: throw IllegalArgumentException("$flag is not a recognized feature flag.")
    }
}