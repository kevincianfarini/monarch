package energy.octopus.featureflag

public class FeatureFlagChangeObserver<T>(
    public var onValueChanged: (T?) -> Unit
)