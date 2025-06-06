package io.github.kevincianfarini.monarch.core

import io.github.kevincianfarini.monarch.test.InMemoryFeatureFlagDataStore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MixinFeatureFlagManagerTest {

    @Test
    fun manager_gets_string_value() {
        val store = InMemoryFeatureFlagDataStore().apply { setString("foo", "bar") }
        assertEquals(
            expected = "bar",
            actual = manager(store).currentValueOf(StringFeature),
        )
    }

    @Test
    fun manager_gets_default_string_value() = assertEquals(
        expected = "blah",
        actual = manager().currentValueOf(StringFeature),
    )

    @Test
    fun manager_gets_boolean_value() {
        val store = InMemoryFeatureFlagDataStore().apply { setBoolean("bool", true) }
        assertTrue(manager(store).currentValueOf(BooleanFeature))
    }

    @Test
    fun manager_gets_default_boolean_value() = assertFalse(
        manager().currentValueOf(
            BooleanFeature
        )
    )

    @Test
    fun manager_gets_double_value() {
        val store = InMemoryFeatureFlagDataStore().apply { setDouble("double", 15.7) }
        assertEquals(
            expected = 15.7,
            actual = manager(store).currentValueOf(DoubleFeature),
            absoluteTolerance = 0.05,
        )
    }

    @Test
    fun manager_gets_default_double_value() = assertEquals(
        expected = 1.5,
        actual = manager().currentValueOf(DoubleFeature),
        absoluteTolerance = 0.05,
    )

    @Test
    fun manager_gets_long_value() {
        val store = InMemoryFeatureFlagDataStore().apply { setLong("long", 27L) }
        assertEquals(
            expected = 27L,
            actual = manager(store).currentValueOf(LongFeature),
        )
    }

    @Test
    fun manager_gets_default_long_value() = assertEquals(
        expected = 1027L,
        actual = manager().currentValueOf(LongFeature),
    )

    @Test
    fun manager_gets_mixin_value() {
        val store = InMemoryFeatureFlagDataStore().apply { setString("some_int", "1") }
        assertEquals(
            expected = 1,
            actual = manager(store, listOf(ObservableIntDecodingMixin)).currentValueOf(
                IntFeatureFlag
            ),
        )
    }

    @Test
    fun manager_errors_with_unrecognized_flag_type() {
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