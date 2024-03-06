package io.github.kevincianfarini.monarch

/**
 * An underlying store of raw, primitive feature flag values.
 */
public interface FeatureFlagDataStore {

    /**
     * Get the [Boolean] value associated with [key] if present. Otherwise, return [default].
     */
    public fun getBoolean(key: String, default: Boolean): Boolean

    /**
     * Get the [String] value associated with [key] if present. Otherwise, return [default].
     */
    public fun getString(key: String, default: String): String

    /**
     * Get the [Double] value associated with [key] if present. Otherwise, return [default].
     */
    public fun getDouble(key: String, default: Double): Double

    /**
     * Get the [Long] value associated with [key] if present. Otherwise, return [default].
     */
    public fun getLong(key: String, default: Long): Long
}