package energy.octopus.featureflag

/**
 * A [FeatureFlagManager] implementation that allows extension via [mixins].
 */
public class MixinFeatureFlagManager(
    /**
     * The datastore which contains raw feature flag data.
     */
    private val store: FeatureFlagDataStore,

    /**
     * A list of [FeatureFlagManagerMixin] that can be queried when this manager is unfit to handle the
     * feature flag query.
     */
    private val mixins: List<FeatureFlagManagerMixin> = emptyList(),
) : FeatureFlagManager {

    @Suppress("UNCHECKED_CAST")
    public override suspend fun <T : FeatureFlagOption> currentValueFor(
        flag: FeatureFlag<T>,
    ): T = when (flag) {
        is BooleanFeatureFlag -> {
            val value = store.getBoolean(flag.key)?.let(flag::optionFrom)
            (value ?: flag.default) as T
        }
        is StringFeatureFlag -> {
            val value = store.getString(flag.key)?.let(flag::optionFrom)
            (value ?: flag.default) as T
        }
        is DoubleFeatureFlag -> {
            val value = store.getDouble(flag.key)?.let(flag::optionFrom)
            (value ?: flag.default) as T
        }
        is LongFeatureFlag -> {
            val value = store.getLong(flag.key)?.let(flag::optionFrom)
            (value ?: flag.default) as T
        }
        is ByteArrayFeatureFlag -> {
            val value = store.getByteArray(flag.key)?.let(flag::optionFrom)
            (value ?: flag.default) as T
        }
        else -> mixins.firstNotNullOfOrNull { delegate ->
            delegate.currentValueForOrNull(flag, store)
        } ?: throw IllegalArgumentException("$flag is not a recognized feature flag.")
    }
}
