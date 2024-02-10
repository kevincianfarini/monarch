package io.github.kevincianfarini.monarch

import kotlin.test.*

class MixinFeatureFlagManagerTest {

    @Test fun `manager gets string value`() {
        val store = FakeFeatureFlagDataStore().apply { setValue("foo", "bar") }
        assertEquals(
            expected = "bar",
            actual = manager(store).currentValueFor(StringFeature),
        )
    }

    @Test fun `manager gets default string value`() = assertEquals(
        expected = "blah",
        actual = manager().currentValueFor(StringFeature),
    )

    @Test fun `manager gets boolean value`() {
        val store = FakeFeatureFlagDataStore().apply { setValue("bool", true) }
        assertTrue(manager(store).currentValueFor(BooleanFeature))
    }

    @Test fun `manager gets default boolean value`() = assertFalse(manager().currentValueFor(BooleanFeature))

    @Test fun `manager gets double value`() {
        val store = FakeFeatureFlagDataStore().apply { setValue("double", 15.7) }
        assertEquals(
            expected = 15.7,
            actual = manager(store).currentValueFor(DoubleFeature),
            absoluteTolerance = 0.05,
        )
    }

    @Test fun `manager gets default double value`() = assertEquals(
        expected = 1.5,
        actual = manager().currentValueFor(DoubleFeature),
        absoluteTolerance = 0.05,
    )

    @Test fun `manager gets long value`() {
        val store = FakeFeatureFlagDataStore().apply { setValue("long", 27L) }
        assertEquals(
            expected = 27L,
            actual = manager(store).currentValueFor(LongFeature),
        )
    }

    @Test fun `manager gets default long value`() = assertEquals(
        expected = 1027L,
        actual = manager().currentValueFor(LongFeature),
    )

    @Test fun `manager gets byte array value`() {
        val store = FakeFeatureFlagDataStore().apply { setValue("byte", byteArrayOf(0b11)) }
        assertContentEquals(
            expected = byteArrayOf(0b11),
            actual = manager(store).currentValueFor(ByteArrayFeature),
        )
    }

    @Test fun `manager gets default byte array value`() = assertContentEquals(
        expected = byteArrayOf(0b1),
        actual = manager().currentValueFor(ByteArrayFeature),
    )

    @Test fun `manager gets mixin value`() {
        val store = FakeFeatureFlagDataStore().apply { setValue("some_int", "1") }
        assertEquals(
            expected = 1,
            actual = manager(store, listOf(ObservableIntDecodingMixin)).currentValueFor(IntFeatureFlag),
        )
    }

    @Test fun `manager errors with unrecognized flag type`() {
        // the below IS NOT a `BooleanOption` and therefore will go unrecognized
        val someRandomFlag = object : FeatureFlag<Boolean> {
            override val key: String = "random_key"
            override val default = false
        }

        assertFailsWith<IllegalArgumentException> {
            manager().currentValueFor(someRandomFlag)
        }
    }

    private fun manager(
        store: FeatureFlagDataStore = FakeFeatureFlagDataStore(),
        mixins: List<FeatureFlagManagerMixin> = emptyList(),
    ) = MixinFeatureFlagManager(store, mixins)
}
