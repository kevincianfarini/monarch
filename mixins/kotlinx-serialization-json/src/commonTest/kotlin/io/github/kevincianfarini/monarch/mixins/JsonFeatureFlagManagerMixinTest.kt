package io.github.kevincianfarini.monarch.mixins

import io.github.kevincianfarini.monarch.BooleanFeatureFlag
import io.github.kevincianfarini.monarch.InMemoryFeatureFlagDataStore
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class JsonFeatureFlagManagerMixinTest {

    @Test fun returns_null_on_unhandled_feature_flag() = assertNull(
        mixin().currentValueOfOrNull(
            flag = NotJson,
            store = InMemoryFeatureFlagDataStore(),
        )
    )

    @Test fun returns_default_on_null_value_result() = assertEquals(
        expected = SomeJsonFlag.default,
        actual = mixin().currentValueOfOrNull(
            flag = SomeJsonFlag,
            store = InMemoryFeatureFlagDataStore(),
        )
    )

    @Test fun returns_deserialized_value() {
        val store = InMemoryFeatureFlagDataStore().apply {
            setValue(
                key = SomeJsonFlag.key,
                value = """{"bar":2}""",
            )
        }
        assertEquals(
            expected = Foo(2),
            actual = mixin().currentValueOfOrNull(
                flag = SomeJsonFlag,
                store = store,
            )
        )
    }

    private fun mixin(json: Json = Json.Default) = JsonFeatureFlagManagerMixin(json)
}

object NotJson : BooleanFeatureFlag(
    key = "not_json",
    default = false,
)