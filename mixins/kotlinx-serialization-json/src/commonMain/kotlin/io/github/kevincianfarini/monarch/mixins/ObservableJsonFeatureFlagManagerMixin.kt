package io.github.kevincianfarini.monarch.mixins

import io.github.kevincianfarini.monarch.FeatureFlag
import io.github.kevincianfarini.monarch.FeatureFlagManagerMixin
import io.github.kevincianfarini.monarch.ObservableFeatureFlagDataStore
import io.github.kevincianfarini.monarch.ObservableFeatureFlagManagerMixin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

public class ObservableJsonFeatureFlagManagerMixin(
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
