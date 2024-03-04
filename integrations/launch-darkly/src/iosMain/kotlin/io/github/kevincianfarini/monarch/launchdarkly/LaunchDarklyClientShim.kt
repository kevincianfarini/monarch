package io.github.kevincianfarini.monarch.launchdarkly

/**
 * A temporary, experimental shim to allow iOS consumers of Monarch to wire their own LDClient
 * as a data store using [LaunchDarklyClientShim.asFeatureFlagDataStore]. This interface will be
 * removed in future versions of this library when future, first-party support of LaunchDarkly
 * is available.
 */
public interface LaunchDarklyClientShim {

    public fun boolVariation(forKey: String, default: Boolean): Boolean

    public fun intVariation(forKey: String, default: Int): Int

    public fun doubleVariation(forKey: String, default: Double): Double

    public fun stringVariation(forKey: String, default: String): String

    public fun jsonStringVariation(forKey: String, default: String?): String?

    public fun observe(key: String, owner: Any, handler: () -> Unit)

    public fun stopObserving(owner: Any)
}