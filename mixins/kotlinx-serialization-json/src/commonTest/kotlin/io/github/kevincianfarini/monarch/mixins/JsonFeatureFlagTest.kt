package io.github.kevincianfarini.monarch.mixins

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonFeatureFlagTest {

    @Test fun deserializes_with_supplied_serializer() {
        val jsonString = """{"bar":2}"""
        assertEquals(
            expected = Foo(2),
            actual = SomeJsonFlag.deserialize(jsonString, Json.Default)
        )
    }
}

object SomeJsonFlag : JsonFeatureFlag<Foo>(
    key = "some_flag",
    default = Foo(1),
    serializer = Foo.serializer(),
)

@Serializable
data class Foo(val bar: Int)