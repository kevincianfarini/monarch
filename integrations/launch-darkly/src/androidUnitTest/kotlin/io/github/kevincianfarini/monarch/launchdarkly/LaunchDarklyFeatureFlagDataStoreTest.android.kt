package io.github.kevincianfarini.monarch.launchdarkly

import com.launchdarkly.sdk.EvaluationDetail
import com.launchdarkly.sdk.LDContext
import com.launchdarkly.sdk.LDValue
import com.launchdarkly.sdk.android.ConnectionInformation
import com.launchdarkly.sdk.android.FeatureFlagChangeListener
import com.launchdarkly.sdk.android.LDAllFlagsListener
import com.launchdarkly.sdk.android.LDClientInterface
import com.launchdarkly.sdk.android.LDStatusListener
import io.github.kevincianfarini.monarch.ObservableFeatureFlagDataStore
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Future

actual fun sut(): Pair<ObservableFeatureFlagDataStore, MutableLDClientInterface> {
    val client = FakeLDClient()
    return Pair(client.asFeatureFlagDataStore(), client)
}

private class FakeLDClient : LDClientInterface, MutableLDClientInterface {

    private val flagValues = ConcurrentHashMap<String, Any>()
    private val listeners = ConcurrentHashMap<String, Set<FeatureFlagChangeListener>>()

    override fun boolVariation(p0: String, p1: Boolean): Boolean {
        return (flagValues[p0] as? Boolean) ?: p1
    }

    override fun intVariation(p0: String, p1: Int): Int {
        return (flagValues[p0] as? Int) ?: p1
    }

    override fun doubleVariation(p0: String, p1: Double): Double {
        return (flagValues[p0] as? Double) ?: p1
    }

    override fun stringVariation(p0: String, p1: String): String {
        return (flagValues[p0] as? String) ?: p1
    }

    override fun registerFeatureFlagListener(p0: String, p1: FeatureFlagChangeListener) {
        val currentListeners = listeners[p0] ?: emptySet()
        listeners[p0] = currentListeners + p1
    }

    override fun unregisterFeatureFlagListener(p0: String, p1: FeatureFlagChangeListener) {
        val currentListeners = listeners[p0] ?: emptySet()
        listeners[p0] = currentListeners - p1
    }

    override fun setVariation(flagKey: String, value: Any) {
        flagValues[flagKey] = value
        listeners[flagKey]?.forEach { it.onFeatureFlagChange(flagKey) }
    }

    override fun boolVariationDetail(p0: String?, p1: Boolean): EvaluationDetail<Boolean> {
        TODO("Not yet implemented")
    }

    override fun stringVariationDetail(p0: String?, p1: String?): EvaluationDetail<String> {
        TODO("Not yet implemented")
    }

    override fun doubleVariationDetail(p0: String?, p1: Double): EvaluationDetail<Double> {
        TODO("Not yet implemented")
    }

    override fun intVariationDetail(p0: String?, p1: Int): EvaluationDetail<Int> {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun isInitialized(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isOffline(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setOffline() {
        TODO("Not yet implemented")
    }

    override fun setOnline() {
        TODO("Not yet implemented")
    }

    override fun trackMetric(p0: String?, p1: LDValue?, p2: Double) {
        TODO("Not yet implemented")
    }

    override fun trackData(p0: String?, p1: LDValue?) {
        TODO("Not yet implemented")
    }

    override fun track(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun identify(p0: LDContext?): Future<Void> {
        TODO("Not yet implemented")
    }

    override fun flush() {
        TODO("Not yet implemented")
    }

    override fun allFlags(): MutableMap<String, LDValue> {
        TODO("Not yet implemented")
    }

    override fun jsonValueVariation(p0: String?, p1: LDValue?): LDValue {
        TODO("Not yet implemented")
    }

    override fun jsonValueVariationDetail(p0: String?, p1: LDValue?): EvaluationDetail<LDValue> {
        TODO("Not yet implemented")
    }

    override fun getConnectionInformation(): ConnectionInformation {
        TODO("Not yet implemented")
    }

    override fun unregisterStatusListener(p0: LDStatusListener?) {
        TODO("Not yet implemented")
    }

    override fun registerStatusListener(p0: LDStatusListener?) {
        TODO("Not yet implemented")
    }

    override fun registerAllFlagsListener(p0: LDAllFlagsListener?) {
        TODO("Not yet implemented")
    }

    override fun unregisterAllFlagsListener(p0: LDAllFlagsListener?) {
        TODO("Not yet implemented")
    }

    override fun isDisableBackgroundPolling(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getVersion(): String {
        TODO("Not yet implemented")
    }
}
