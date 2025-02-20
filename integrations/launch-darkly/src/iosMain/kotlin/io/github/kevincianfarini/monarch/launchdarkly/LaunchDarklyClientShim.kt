package io.github.kevincianfarini.monarch.launchdarkly

/**
 * A temporary, experimental shim to allow iOS consumers of Monarch to wire their own LDClient
 * as a data store using [LaunchDarklyClientShim.asFeatureFlagDataStore]. This interface will be
 * removed in future versions of this library when future, first-party support of LaunchDarkly
 * is available.
 */
public interface LaunchDarklyClientShim {

    /**
     * Return a [Boolean] value [forKey], or [default] if no value exists.
     */
    public fun boolVariation(forKey: String, default: Boolean): Boolean

    /**
     * Return an [Int] value [forKey], or [default] if no value exists.
     */
    public fun intVariation(forKey: String, default: Int): Int

    /**
     * Return a [Double] value [forKey], or [default] if no value exists.
     */
    public fun doubleVariation(forKey: String, default: Double): Double

    /**
     * Return a [String] value [forKey], or [default] if no value exists.
     */
    public fun stringVariation(forKey: String, default: String): String

    /**
     * Register a [handler] to be invoked when the value associated with [key] changes, scoped
     * to [owner].
     */
    public fun observe(key: String, owner: ObserverOwner, handler: () -> Unit)

    /**
     * Unregister all observers scoped to [owner].
     */
    public fun stopObserving(owner: ObserverOwner)
}

/**
 * A marker object used in [LaunchDarklyClientShim.observe] and
 * [LaunchDarklyClientShim.stopObserving].
 */
public class ObserverOwner internal constructor()