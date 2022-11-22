package energy.octopus.monarch.mixins

import energy.octopus.monarch.FeatureFlag
import energy.octopus.monarch.FeatureFlagDataStore
import energy.octopus.monarch.FeatureFlagManagerMixin
import kotlinx.serialization.json.Json

public class JsonFeatureFlagManagerMixin(
    private val json: Json,
) : FeatureFlagManagerMixin {

    public override fun <T : Any> currentValueForOrNull(
        flag: FeatureFlag<T>,
        store: FeatureFlagDataStore,
    ): T? = when (flag) {
        is JsonFeatureFlag<T> -> {
            val value = store.getString(flag.key)?.let { flag.deserialize(it, json) }
            (value ?: flag.default)
        }
        else -> null
    }
}