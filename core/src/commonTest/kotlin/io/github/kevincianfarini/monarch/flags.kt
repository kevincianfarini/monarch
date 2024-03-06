@file:Suppress("UNCHECKED_CAST")

package io.github.kevincianfarini.monarch

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object StringFeature : StringFeatureFlag(key = "foo", default = "blah")
object BooleanFeature : BooleanFeatureFlag(key = "bool", default = false)
object DoubleFeature : DoubleFeatureFlag(key = "double", default = 1.5)
object LongFeature : LongFeatureFlag(key = "long", default = 1027L)
object IntFeatureFlag : FeatureFlag<Int> {
    override val key: String get() = "some_int"
    override val default = -1
}


// FeatureFlagManager mixin that handles IntFeatureFlags, returns Flow<IntOption>
object ObservableIntDecodingMixin : ObservableFeatureFlagManagerMixin {
    override fun <T : Any> valuesOfOrNull(
        flag: FeatureFlag<T>,
        store: ObservableFeatureFlagDataStore,
    ): Flow<T>? = when (flag) {
        is IntFeatureFlag -> store.observeString(flag.key, flag.default.toString()).map { str ->
            str.toInt()
        } as Flow<T>
        else -> null
    }

    override fun <T : Any> currentValueOfOrNull(
        flag: FeatureFlag<T>,
        store: FeatureFlagDataStore
    ): T? = when (flag) {
        is IntFeatureFlag -> store.getString(flag.key, flag.default.toString()).toInt() as T
        else -> null
    }
}
