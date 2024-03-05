package io.github.kevincianfarini.monarch.mixins

import io.github.kevincianfarini.monarch.FeatureFlag
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

/**
 * An implementation of [FeatureFlag] that supports deserializing JSON strings to [OptionType].
 */
public abstract class JsonFeatureFlag<OptionType : Any>(
    public override val key: String,
    public override val default: OptionType,

    /**
     * The [KSerializer] used to marshall objects between their JSON string representation and their object
     * representation.
     */
    private val serializer: KSerializer<OptionType>,
) : FeatureFlag<OptionType> {

    internal fun deserialize(raw: String, json: Json): OptionType {
        return json.decodeFromString(serializer, raw)
    }

    internal fun serializedDefault(json: Json): String {
        return json.encodeToString(serializer, default)
    }
}
