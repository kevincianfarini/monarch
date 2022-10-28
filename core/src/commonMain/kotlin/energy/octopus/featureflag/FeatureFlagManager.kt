package energy.octopus.featureflag

/**
 * Manage the options a [FeatureFlag] can manifest as.
 */
public interface FeatureFlagManager {

    /**
     * Get the current [FeatureFlagOption] value of [flag].
     */
    public suspend fun <T : FeatureFlagOption> currentValueFor(
        flag: FeatureFlag<T>,
    ): T
}
