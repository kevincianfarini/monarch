package energy.octopus.monarch

import kotlinx.coroutines.flow.Flow

public interface ObservableFeatureFlagManager : FeatureFlagManager {

    /**
     * Emits value changes of [flag].
     */
    public fun <T : Any> valuesFor(
        flag: FeatureFlag<T>,
    ): Flow<T>
}
