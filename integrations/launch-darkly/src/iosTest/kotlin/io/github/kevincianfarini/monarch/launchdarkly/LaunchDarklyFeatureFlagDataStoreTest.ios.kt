package io.github.kevincianfarini.monarch.launchdarkly

import io.github.kevincianfarini.monarch.ObservableFeatureFlagDataStore

actual fun sut(): Pair<ObservableFeatureFlagDataStore, MutableLDClientInterface> {
    val client = FakeLDShim()
    return Pair(client.asFeatureFlagDataStore(), client)
}

private class FakeLDShim : LaunchDarklyClientShim, MutableLDClientInterface {

    private val flagValues = mutableMapOf<String, Any>()
    private val listeners = mutableSetOf<FlagListener>()

    override fun setVariation(flagKey: String, value: Boolean) {
        flagValues[flagKey] = value
        listeners.filter { it.key == flagKey }.forEach { it.handler() }
    }

    override fun setVariation(flagKey: String, value: String) {
        flagValues[flagKey] = value
        listeners.filter { it.key == flagKey }.forEach { it.handler() }
    }

    override fun setVariation(flagKey: String, value: Double) {
        flagValues[flagKey] = value
        listeners.filter { it.key == flagKey }.forEach { it.handler() }
    }

    override fun setVariation(flagKey: String, value: Int) {
        flagValues[flagKey] = value
        listeners.filter { it.key == flagKey }.forEach { it.handler() }
    }

    override fun boolVariation(forKey: String, default: Boolean): Boolean {
        return (flagValues[forKey] as? Boolean) ?: default
    }

    override fun intVariation(forKey: String, default: Int): Int {
        return (flagValues[forKey] as? Int) ?: default
    }

    override fun doubleVariation(forKey: String, default: Double): Double {
        return (flagValues[forKey] as? Double) ?: default
    }

    override fun stringVariation(forKey: String, default: String): String {
        return (flagValues[forKey] as? String) ?: default
    }

    override fun observe(key: String, owner: ObserverOwner, handler: () -> Unit) {
        listeners.add(FlagListener(key, owner, handler))
    }

    override fun stopObserving(owner: ObserverOwner) {
        listeners.removeAll { it.owner === owner }
    }
}

private data class FlagListener(
    val key: String,
    val owner: ObserverOwner,
    val handler: () -> Unit,
)
