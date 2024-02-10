package io.github.kevincianfarini.monarch

import kotlinx.coroutines.flow.Flow

/**
 * A generic definition of a raw feature flag datastore that supports observability
 *
 * NOTE: this uses callbacks vs Flows to maintain compatibility with Swift
 */
public interface ObservableFeatureFlagDataStore : FeatureFlagDataStore {

    /**
     * Observe the [String] value associated with [key] if present. Otherwise, emits null.
     */
    public fun observeString(key: String): Flow<String?>

    /**
     * Observe the [Boolean] value associated with [key] if present. Otherwise, emits null.
     */
    public fun observeBoolean(key: String): Flow<Boolean?>

    /**
     * Observe the [Double] value associated with [key] if present. Otherwise, emits null.
     */
    public fun observeDouble(key: String): Flow<Double?>

    /**
     * Observe the [Long] value associated with [key] if present. Otherwise, emits null.
     */
    public fun observeLong(key: String): Flow<Long?>

    /**
     * Observe the [ByteArray] value associated with [key] if present. Otherwise, emits null.
     */
    public fun observeByteArray(key: String): Flow<ByteArray?>
}