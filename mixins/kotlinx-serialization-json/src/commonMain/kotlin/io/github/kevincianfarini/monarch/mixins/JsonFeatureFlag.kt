package io.github.kevincianfarini.monarch.mixins

import io.github.kevincianfarini.monarch.FeatureFlag
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

public abstract class JsonFeatureFlag<OptionType : Any>(
    public override val key: String,
    public override val default: OptionType,
    private val serializer: KSerializer<OptionType>,
) : FeatureFlag<OptionType> {

    internal fun deserialize(raw: String, json: Json): OptionType {
        return json.decodeFromString(serializer, raw)
    }

    internal fun serializedDefault(json: Json): String {
        return json.encodeToString(serializer, default)
    }
}
