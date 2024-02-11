package io.github.kevincianfarini.monarch.mixins

import io.github.kevincianfarini.monarch.FeatureFlag
import io.github.kevincianfarini.monarch.FeatureFlagDataStore
import io.github.kevincianfarini.monarch.FeatureFlagManagerMixin
import kotlinx.serialization.json.Json

public class JsonFeatureFlagManagerMixin(
    private val json: Json,
) : FeatureFlagManagerMixin {

    public override fun <T : Any> currentValueOfOrNull(
        flag: FeatureFlag<T>,
        store: FeatureFlagDataStore,
    ): T? = when (flag) {
        is JsonFeatureFlag<T> -> store.getString(flag.key, flag.serializedDefault(json)).let { string ->
            flag.deserialize(string, json)
        }
        else -> null
    }
}