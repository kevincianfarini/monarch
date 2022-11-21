package energy.octopus.monarch

public class FeatureFlagChangeObserver<T>(
    public var onValueChanged: (T?) -> Unit
)