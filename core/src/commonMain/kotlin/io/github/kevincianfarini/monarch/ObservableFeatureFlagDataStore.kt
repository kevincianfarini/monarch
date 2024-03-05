package io.github.kevincianfarini.monarch

import kotlinx.coroutines.flow.Flow

/**
 * An underlying store of raw, primitive feature flag values that supports observability via [Flow].
 */
public interface ObservableFeatureFlagDataStore : FeatureFlagDataStore {

    /**
     * Return a [Flow] which emits updates to a [String] value associated with [key] if present. When no value is
     * present for [key], the returned flow will emit [default]. The returned flow initially emits the value at the
     * time of collection, and will emit again on each subsequent update for [key].
     */
    public fun observeString(key: String, default: String): Flow<String>

    /**
     * Return a [Flow] which emits updates to a [Boolean] value associated with [key] if present. When no value is
     * present for [key], the returned flow will emit [default]. The returned flow initially emits the value at the
     * time of collection, and will emit again on each subsequent update for [key].
     */
    public fun observeBoolean(key: String, default: Boolean): Flow<Boolean>

    /**
     * Return a [Flow] which emits updates to a [Double] value associated with [key] if present. When no value is
     * present for [key], the returned flow will emit [default]. The returned flow initially emits the value at the
     * time of collection, and will emit again on each subsequent update for [key].
     */
    public fun observeDouble(key: String, default: Double): Flow<Double>

    /**
     * Return a [Flow] which emits updates to a [Long] value associated with [key] if present. When no value is
     * present for [key], the returned flow will emit [default]. The returned flow initially emits the value at the
     * time of collection, and will emit again on each subsequent update for [key].
     */
    public fun observeLong(key: String, default: Long): Flow<Long>

    /**
     * Return a [Flow] which emits updates to a [ByteArray] value associated with [key] if present. When no value is
     * present for [key], the returned flow will emit [default]. The returned flow initially emits the value at the
     * time of collection, and will emit again on each subsequent update for [key].
     */
    public fun observeByteArray(key: String, default: ByteArray): Flow<ByteArray>
}