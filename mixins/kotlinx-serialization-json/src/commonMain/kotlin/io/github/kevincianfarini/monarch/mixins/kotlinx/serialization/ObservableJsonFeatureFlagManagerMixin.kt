package io.github.kevincianfarini.monarch.mixins.kotlinx.serialization

import io.github.kevincianfarini.monarch.core.FeatureFlag
import io.github.kevincianfarini.monarch.core.FeatureFlagManagerMixin
import io.github.kevincianfarini.monarch.core.ObservableFeatureFlagDataStore
import io.github.kevincianfarini.monarch.core.ObservableFeatureFlagManagerMixin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

/**
 * An implementation of [io.github.kevincianfarini.monarch.core.ObservableFeatureFlagManagerMixin] that adds support for [JsonFeatureFlag] instances.
 */
public class ObservableJsonFeatureFlagManagerMixin(
    /**
     * The [Json] instance used to serialize and deserialze feature flag values. This value should adhere to the JSON
     * requirements imposed by your [io.github.kevincianfarini.monarch.core.FeatureFlagDataStore] implementation's underlying data source.
     */
    private val json: Json,
) : ObservableFeatureFlagManagerMixin, FeatureFlagManagerMixin by JsonFeatureFlagManagerMixin(json) {

    public override fun <T : Any> valuesOfOrNull(
        flag: FeatureFlag<T>,
        store: ObservableFeatureFlagDataStore
    ): Flow<T>? = when (flag) {
        is JsonFeatureFlag<T> -> store.observeString(flag.key, flag.serializedDefault(json)).map { string ->
            flag.deserialize(string, json)
        }
        else -> null
    }
}
