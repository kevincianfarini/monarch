package energy.octopus.monarch

/**
 * A generic definition of a raw feature flag datastore.
 */
public interface FeatureFlagDataStore {

    /**
     * Get the [Boolean] value associated with [key] if present. Otherwise, return null.
     */
    public fun getBoolean(key: String): Boolean?

    /**
     * Get the [String] value associated with [key] if present. Otherwise, return null.
     */
    public fun getString(key: String): String?

    /**
     * Get the [Double] value associated with [key] if present. Otherwise, return null.
     */
    public fun getDouble(key: String): Double?

    /**
     * Get the [Long] value associated with [key] if present. Otherwise, return null.
     */
    public fun getLong(key: String): Long?

    /**
     * Get the [ByteArray] value associated with [key] if present. Otherwise, return null.
     */
    public fun getByteArray(key: String): ByteArray?
}