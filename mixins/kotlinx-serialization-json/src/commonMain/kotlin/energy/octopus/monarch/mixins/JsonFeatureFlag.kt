package energy.octopus.monarch.mixins

import energy.octopus.monarch.FeatureFlag
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json

public abstract class JsonFeatureFlag<OptionType : Any>(
    public override val key: String,
    public override val default: OptionType,
    private val deserializer: DeserializationStrategy<OptionType>,
) : FeatureFlag<OptionType> {

    internal fun deserialize(raw: String, json: Json): OptionType {
        return json.decodeFromString(deserializer, raw)
    }
}
