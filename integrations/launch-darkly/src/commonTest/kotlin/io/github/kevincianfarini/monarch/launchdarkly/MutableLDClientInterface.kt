package io.github.kevincianfarini.monarch.launchdarkly

interface MutableLDClientInterface {

    fun setVariation(flagKey: String, value: Any)
}

