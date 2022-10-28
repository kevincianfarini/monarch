package energy.octopus.featureflag

/**
 * A generic definition of a raw feature flag datastore that supports observability
 *
 * NOTE: this uses callbacks vs Flows to maintain compatibility with Swift
 */
public interface ObservableFeatureFlagDataStore : FeatureFlagDataStore {

    /**
     * Observe the [String] value associated with [key] if present. Otherwise, emits null.
     */
    public fun observeString(key: String, observer: FeatureFlagChangeObserver<String?>)

    /**
     * Observe the [Boolean] value associated with [key] if present. Otherwise, emits null.
     */
    public fun observeBoolean(key: String, observer: FeatureFlagChangeObserver<Boolean?>)

    /**
     * Observe the [Double] value associated with [key] if present. Otherwise, emits null.
     */
    public fun observeDouble(key: String, observer: FeatureFlagChangeObserver<Double?>)

    /**
     * Observe the [Long] value associated with [key] if present. Otherwise, emits null.
     */
    public fun observeLong(key: String, observer: FeatureFlagChangeObserver<Long?>)

    /**
     * Observe the [ByteArray] value associated with [key] if present. Otherwise, emits null.
     */
    public fun observeByteArray(key: String, observer: FeatureFlagChangeObserver<ByteArray?>)

    /**
     * Stop observing the value associated with [key] - ensure `[onValueChanged] is the same
     * lambda passed to [observe*] function
     */
    public fun <T> removeObserver(key: String, observer: FeatureFlagChangeObserver<T>)
}