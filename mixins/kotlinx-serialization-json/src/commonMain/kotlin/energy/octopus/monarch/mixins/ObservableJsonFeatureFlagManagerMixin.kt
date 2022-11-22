package energy.octopus.monarch.mixins

import energy.octopus.monarch.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

public class ObservableJsonFeatureFlagManagerMixin(
    private val json: Json,
) : ObservableFeatureFlagManagerMixin, FeatureFlagManagerMixin by JsonFeatureFlagManagerMixin(json) {

    public override fun <T : Any> valuesOrNull(
        flag: FeatureFlag<T>,
        store: ObservableFeatureFlagDataStore
    ): Flow<T>? = when (flag) {
        is JsonFeatureFlag<T> -> store.observeString(flag.key).map { string ->
            string?.let { flag.deserialize(it, json) } ?: flag.default
        }
        else -> null
    }
}
