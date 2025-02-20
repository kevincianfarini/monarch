package io.github.kevincianfarini.monarch.launchdarkly

interface MutableLDClientInterface {
    fun setVariation(flagKey: String, value: Boolean)
    fun setVariation(flagKey: String, value: String)
    fun setVariation(flagKey: String, value: Double)
    fun setVariation(flagKey: String, value: Int)
}

