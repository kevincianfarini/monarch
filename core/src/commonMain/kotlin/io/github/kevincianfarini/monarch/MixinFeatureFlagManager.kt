package io.github.kevincianfarini.monarch

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
    public override fun <T : Any> currentValueOf(flag: FeatureFlag<T>): T = when (flag) {
        is BooleanFeatureFlag -> store.getBoolean(flag.key, flag.default) as T
        is StringFeatureFlag -> store.getString(flag.key, flag.default) as T
        is DoubleFeatureFlag -> store.getDouble(flag.key, flag.default) as T
        is LongFeatureFlag -> store.getLong(flag.key, flag.default) as T
        is ByteArrayFeatureFlag -> store.getByteArray(flag.key, flag.default) as T
        else -> mixins.firstNotNullOfOrNull { delegate ->
            delegate.currentValueOfOrNull(flag, store)
        } ?: throw IllegalArgumentException("$flag is not a recognized feature flag.")
    }
}
