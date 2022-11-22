package energy.octopus.monarch.mixins

import energy.octopus.monarch.BooleanFeatureFlag
import energy.octopus.monarch.FakeFeatureFlagDataStore
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class JsonFeatureFlagManagerMixinTest {

    @Test fun `returns null on unhandled feature flag`() = assertNull(
        mixin().currentValueForOrNull(
            flag = NotJson,
            store = FakeFeatureFlagDataStore(),
        )
    )

    @Test fun `returns default on null value result`() = assertEquals(
        expected = SomeJsonFlag.default,
        actual = mixin().currentValueForOrNull(
            flag = SomeJsonFlag,
            store = FakeFeatureFlagDataStore(),
        )
    )

    @Test fun `returns deserialized value`() {
        val store = FakeFeatureFlagDataStore().apply {
            setValue(
                key = SomeJsonFlag.key,
                value = """{"bar":2}""",
            )
        }
        assertEquals(
            expected = Foo(2),
            actual = mixin().currentValueForOrNull(
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