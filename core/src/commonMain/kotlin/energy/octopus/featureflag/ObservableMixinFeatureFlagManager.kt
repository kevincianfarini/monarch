package energy.octopus.featureflag

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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
    public override fun <T : FeatureFlagOption> valuesFor(flag: FeatureFlag<T>): Flow<T> = when (flag) {
        is BooleanFeatureFlag -> callbackFlow {
            val onValueChanged: ((Boolean?) -> Unit) = {
                trySend((it?.let(flag::optionFrom) ?: flag.default) as T)
            }
            val observer = FeatureFlagChangeObserver(onValueChanged = onValueChanged)
            store.observeBoolean(flag.key, observer)
            awaitClose { store.removeObserver(flag.key, observer) }
        }
        is StringFeatureFlag -> callbackFlow {
            val onValueChanged: ((String?) -> Unit) = {
                trySend((it?.let(flag::optionFrom) ?: flag.default) as T)
            }
            val observer = FeatureFlagChangeObserver(onValueChanged = onValueChanged)
            store.observeString(flag.key, observer)
            awaitClose { store.removeObserver(flag.key, observer) }
        }
        is DoubleFeatureFlag -> callbackFlow {
            val onValueChanged: ((Double?) -> Unit) = {
                trySend((it?.let(flag::optionFrom) ?: flag.default) as T)
            }
            val observer = FeatureFlagChangeObserver(onValueChanged = onValueChanged)
            store.observeDouble(flag.key, observer)
            awaitClose { store.removeObserver(flag.key, observer) }
        }
        is LongFeatureFlag -> callbackFlow {
            val onValueChanged: ((Long?) -> Unit) = {
                trySend((it?.let(flag::optionFrom) ?: flag.default) as T)
            }
            val observer = FeatureFlagChangeObserver(onValueChanged = onValueChanged)
            store.observeLong(flag.key, observer)
            awaitClose { store.removeObserver(flag.key, observer) }
        }
        is ByteArrayFeatureFlag -> callbackFlow {
            val onValueChanged: ((ByteArray?) -> Unit) = {
                trySend((it?.let(flag::optionFrom) ?: flag.default) as T)
            }
            val observer = FeatureFlagChangeObserver(onValueChanged = onValueChanged)
            store.observeByteArray(flag.key, observer)
            awaitClose { store.removeObserver(flag.key, observer) }
        }
        else -> mixins.firstNotNullOfOrNull { delegate ->
            delegate.valuesOrNull(flag, store)
        } ?: throw IllegalArgumentException("$flag is not a recognized feature flag.")
    }
}