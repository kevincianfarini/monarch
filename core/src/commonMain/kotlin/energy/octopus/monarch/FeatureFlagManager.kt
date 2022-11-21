package energy.octopus.monarch

/**
 * Manage the options a [FeatureFlag] can manifest as.
 */
public interface FeatureFlagManager {

    /**
     * Get the current value of [flag].
     */
    public suspend fun <T : Any> currentValueFor(
        flag: FeatureFlag<T>,
    ): T
}
