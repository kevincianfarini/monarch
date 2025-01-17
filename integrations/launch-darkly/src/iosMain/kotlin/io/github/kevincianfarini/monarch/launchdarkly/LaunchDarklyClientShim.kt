package io.github.kevincianfarini.monarch.launchdarkly

import LaunchDarkly.LDClient
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * An interface around [LDClient].
 */
internal interface LaunchDarklyClientShim {

    /**
     * Return a [Boolean] value [forKey], or [default] if no value exists.
     */
    fun boolVariation(forKey: String, default: Boolean): Boolean

    /**
     * Return an [Int] value [forKey], or [default] if no value exists.
     */
    fun longVariation(forKey: String, default: Long): Long

    /**
     * Return a [Double] value [forKey], or [default] if no value exists.
     */
    fun doubleVariation(forKey: String, default: Double): Double

    /**
     * Return a [String] value [forKey], or [default] if no value exists.
     */
    fun stringVariation(forKey: String, default: String): String

    /**
     * Register a [handler] to be invoked when the value associated with [key] changes, scoped
     * to [owner].
     */
    fun observe(key: String, owner: ObserverOwner, handler: () -> Unit)

    /**
     * Unregister all observers scoped to [owner].
     */
    fun stopObserving(owner: ObserverOwner)
}

/**
 * A marker object used in [LaunchDarklyClientShim.observe] and
 * [LaunchDarklyClientShim.stopObserving].
 */
internal class ObserverOwner internal constructor()

@OptIn(ExperimentalForeignApi::class)
internal class RealLaunchDarklyShim(private val client: LDClient) : LaunchDarklyClientShim {

    override fun boolVariation(forKey: String, default: Boolean): Boolean {
        return client.boolVariationForKey(forKey, default)
    }

    override fun longVariation(forKey: String, default: Long): Long {
        return client.integerVariationForKey(forKey, default)
    }

    override fun doubleVariation(forKey: String, default: Double): Double {
        return client.doubleVariationForKey(forKey, default)
    }

    override fun stringVariation(forKey: String, default: String): String {
        return client.stringVariationForKey(forKey, default)
    }

    override fun observe(key: String, owner: ObserverOwner, handler: () -> Unit) {
        client.observe(key, owner) { handler() }
    }

    override fun stopObserving(owner: ObserverOwner) {
        client.stopObservingForOwner(owner)
    }
}