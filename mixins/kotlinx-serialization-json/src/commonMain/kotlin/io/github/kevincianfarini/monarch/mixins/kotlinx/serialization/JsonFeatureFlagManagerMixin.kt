package io.github.kevincianfarini.monarch.mixins.kotlinx.serialization

import io.github.kevincianfarini.monarch.core.FeatureFlag
import io.github.kevincianfarini.monarch.core.FeatureFlagDataStore
import io.github.kevincianfarini.monarch.core.FeatureFlagManagerMixin
import kotlinx.serialization.json.Json

/**
 * An implementation of [FeatureFlagManagerMixin] that adds support for [JsonFeatureFlag] instances.
 */
public class JsonFeatureFlagManagerMixin(
    /**
     * The [Json] instance used to serialize and deserialze feature flag values. This value should adhere to the JSON
     * requirements imposed by your [FeatureFlagDataStore] implementation's underlying data source.
     */
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