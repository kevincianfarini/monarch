package energy.octopus.monarch.mixins

import energy.octopus.monarch.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json

public class ObservableJsonFeatureFlagManagerMixin(
    private val json: Json,
) : ObservableFeatureFlagManagerMixin, FeatureFlagManagerMixin by JsonFeatureFlagManagerMixin(json) {

    public override fun <T : Any> valuesOrNull(
        flag: FeatureFlag<T>,
        store: ObservableFeatureFlagDataStore
    ): Flow<T>? = when (flag) {
        is JsonFeatureFlag<T> -> callbackFlow {
            val onValueChanged: ((String?) -> Unit) = {
                trySend(it?.let { flag.deserialize(it, json) } ?: flag.default)
            }
            val observer = FeatureFlagChangeObserver(onValueChanged = onValueChanged)
            store.observeString(flag.key, observer)
            awaitClose { store.removeObserver(flag.key, observer) }
        }
        else -> null
    }
}
