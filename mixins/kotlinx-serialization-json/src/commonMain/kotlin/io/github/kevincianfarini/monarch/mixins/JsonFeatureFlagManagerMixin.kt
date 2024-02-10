package io.github.kevincianfarini.monarch.mixins

import io.github.kevincianfarini.monarch.FeatureFlag
import io.github.kevincianfarini.monarch.FeatureFlagDataStore
import io.github.kevincianfarini.monarch.FeatureFlagManagerMixin
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