package io.github.kevincianfarini.monarch

import kotlin.test.*

class MixinFeatureFlagManagerTest {

    @Test fun manager_gets_string_value() {
        val store = InMemoryFeatureFlagDataStore().apply { setValue("foo", "bar") }
        assertEquals(
            expected = "bar",
            actual = manager(store).currentValueOf(StringFeature),
        )
    }

    @Test fun manager_gets_default_string_value() = assertEquals(
        expected = "blah",
        actual = manager().currentValueOf(StringFeature),
    )

    @Test fun manager_gets_boolean_value() {
        val store = InMemoryFeatureFlagDataStore().apply { setValue("bool", true) }
        assertTrue(manager(store).currentValueOf(BooleanFeature))
    }

    @Test fun manager_gets_default_boolean_value() = assertFalse(manager().currentValueOf(BooleanFeature))

    @Test fun manager_gets_double_value() {
        val store = InMemoryFeatureFlagDataStore().apply { setValue("double", 15.7) }
        assertEquals(
            expected = 15.7,
            actual = manager(store).currentValueOf(DoubleFeature),
            absoluteTolerance = 0.05,
        )
    }

    @Test fun manager_gets_default_double_value() = assertEquals(
        expected = 1.5,
        actual = manager().currentValueOf(DoubleFeature),
        absoluteTolerance = 0.05,
    )

    @Test fun manager_gets_long_value() {
        val store = InMemoryFeatureFlagDataStore().apply { setValue("long", 27L) }
        assertEquals(
            expected = 27L,
            actual = manager(store).currentValueOf(LongFeature),
        )
    }

    @Test fun manager_gets_default_long_value() = assertEquals(
        expected = 1027L,
        actual = manager().currentValueOf(LongFeature),
    )

    @Test fun manager_gets_mixin_value() {
        val store = InMemoryFeatureFlagDataStore().apply { setValue("some_int", "1") }
        assertEquals(
            expected = 1,
            actual = manager(store, listOf(ObservableIntDecodingMixin)).currentValueOf(IntFeatureFlag),
        )
    }

    @Test fun manager_errors_with_unrecognized_flag_type() {
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
