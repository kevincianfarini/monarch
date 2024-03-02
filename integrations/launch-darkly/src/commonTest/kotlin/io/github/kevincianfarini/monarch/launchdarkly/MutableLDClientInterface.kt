package io.github.kevincianfarini.monarch.launchdarkly

import kotlinx.serialization.SerializationStrategy

interface MutableLDClientInterface {

    fun setVariation(flagKey: String, value: Boolean)
    fun setVariation(flagKey: String, value: String)
    fun setVariation(flagKey: String, value: Double)
    fun setVariation(flagKey: String, value: Int)
    fun <T> setVariation(flagKey: String, value: T, serialzer: SerializationStrategy<T>)
}

