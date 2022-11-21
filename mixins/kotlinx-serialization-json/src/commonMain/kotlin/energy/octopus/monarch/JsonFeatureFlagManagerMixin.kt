package energy.octopus.monarch

import kotlinx.serialization.json.Json

public class JsonFeatureFlagManagerMixin(
    private val json: Json,
) : FeatureFlagManagerMixin {

    public override suspend fun <T : Any> currentValueForOrNull(
        flag: FeatureFlag<T>,
        store: FeatureFlagDataStore,
    ): T? = when (flag) {
        is JsonFeatureFlag<T> -> {
            val value = store.getString(flag.key)?.let { flag.optionFrom(it, json) }
            (value ?: flag.default)
        }
        else -> null
    }
}