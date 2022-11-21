package energy.octopus.featureflag

import kotlinx.serialization.json.Json

/**
 * A feature flag backed by data encoded as JSON.
 */
public interface JsonFeatureFlag<OptionType : Any> : FeatureFlag<OptionType> {

    /**
     * Adapt a [raw] JSON string into [OptionType] using the [json] configuration.
     */
    public fun optionFrom(raw: String, json: Json): OptionType
}
