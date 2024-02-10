package io.github.kevincianfarini.monarch

import kotlin.test.*

class MixinFeatureFlagManagerTest {

    @Test fun `manager gets string value`() {
        val store = InMemoryFeatureFlagDataStore().apply { setValue("foo", "bar") }
        assertEquals(
            expected = "bar",
            actual = manager(store).currentValueOf(StringFeature),
        )
    }

    @Test fun `manager gets default string value`() = assertEquals(
        expected = "blah",
        actual = manager().currentValueOf(StringFeature),
    )

    @Test fun `manager gets boolean value`() {
        val store = InMemoryFeatureFlagDataStore().apply { setValue("bool", true) }
        assertTrue(manager(store).currentValueOf(BooleanFeature))
    }

    @Test fun `manager gets default boolean value`() = assertFalse(manager().currentValueOf(BooleanFeature))

    @Test fun `manager gets double value`() {
        val store = InMemoryFeatureFlagDataStore().apply { setValue("double", 15.7) }
        assertEquals(
            expected = 15.7,
            actual = manager(store).currentValueOf(DoubleFeature),
            absoluteTolerance = 0.05,
        )
    }

    @Test fun `manager gets default double value`() = assertEquals(
        expected = 1.5,
        actual = manager().currentValueOf(DoubleFeature),
        absoluteTolerance = 0.05,
    )

    @Test fun `manager gets long value`() {
        val store = InMemoryFeatureFlagDataStore().apply { setValue("long", 27L) }
        assertEquals(
            expected = 27L,
            actual = manager(store).currentValueOf(LongFeature),
        )
    }

    @Test fun `manager gets default long value`() = assertEquals(
        expected = 1027L,
        actual = manager().currentValueOf(LongFeature),
    )

    @Test fun `manager gets byte array value`() {
        val store = InMemoryFeatureFlagDataStore().apply { setValue("byte", byteArrayOf(0b11)) }
        assertContentEquals(
            expected = byteArrayOf(0b11),
            actual = manager(store).currentValueOf(ByteArrayFeature),
        )
    }

    @Test fun `manager gets default byte array value`() = assertContentEquals(
        expected = byteArrayOf(0b1),
        actual = manager().currentValueOf(ByteArrayFeature),
    )

    @Test fun `manager gets mixin value`() {
        val store = InMemoryFeatureFlagDataStore().apply { setValue("some_int", "1") }
        assertEquals(
            expected = 1,
            actual = manager(store, listOf(ObservableIntDecodingMixin)).currentValueOf(IntFeatureFlag),
        )
    }

    @Test fun `manager errors with unrecognized flag type`() {
        // the below IS NOT a `BooleanOption` and therefore will go unrecognized
        val someRandomFlag = object : FeatureFlag<Boolean> {
            override val key: String = "random_key"
            override val default = false
        }

        assertFailsWith<IllegalArgumentException> {
            manager().currentValueOf(someRandomFlag)
        }
    }

    private fun manager(
        store: FeatureFlagDataStore = InMemoryFeatureFlagDataStore(),
        mixins: List<FeatureFlagManagerMixin> = emptyList(),
    ) = MixinFeatureFlagManager(store, mixins)
}
