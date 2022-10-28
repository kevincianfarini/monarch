package energy.octopus.featureflag

import kotlinx.coroutines.flow.Flow

public interface ObservableFeatureFlagManager : FeatureFlagManager {

    /**
     * Emits changes to [FeatureFlagOption] value of [flag]
     */
    public fun <T : FeatureFlagOption> valuesFor(
        flag: FeatureFlag<T>,
    ): Flow<T>
}
