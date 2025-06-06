package io.github.kevincianfarini.monarch.core

import app.cash.turbine.test
import io.github.kevincianfarini.monarch.test.InMemoryFeatureFlagDataStore
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ObservableMixinFeatureFlagManagerTest {

    @Test
    fun manager_gets_string_value() {
        runTest {
            val store = InMemoryFeatureFlagDataStore().apply { setString("foo", "bar") }
            manager(store).valuesOf(StringFeature).test {
                assertEquals("bar", awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun manager_gets_default_string_value() {
        runTest {
            manager().valuesOf(StringFeature).test {
                assertEquals("blah", awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun manager_gets_boolean_value() {
        runTest {
            val store = InMemoryFeatureFlagDataStore().apply { setBoolean("bool", true) }
            manager(store).valuesOf(BooleanFeature).test {
                assertTrue(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun manager_gets_default_boolean_value() {
        runTest {
            manager().valuesOf(BooleanFeature).test {
                assertFalse(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun manager_gets_double_value() {
        runTest {
            val store = InMemoryFeatureFlagDataStore().apply { setDouble("double", 15.7) }
            manager(store).valuesOf(DoubleFeature).test {
                assertEquals(expected = 15.7, actual = awaitItem(), absoluteTolerance = 0.05)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun manager_gets_default_double_value() {
        runTest {
            manager().valuesOf(DoubleFeature).test {
                assertEquals(expected = 1.5, actual = awaitItem(), absoluteTolerance = 0.05)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun manager_gets_long_value() {
        runTest {
            val store = InMemoryFeatureFlagDataStore().apply { setLong("long", 27L) }
            manager(store).valuesOf(LongFeature).test {
                assertEquals(expected = 27L, actual = awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun manager_gets_default_long_value() {
        runTest {
            manager().valuesOf(LongFeature).test {
                assertEquals(expected = 1027L, actual = awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun manager_gets_mixin_value() {
        runTest {
            val store = InMemoryFeatureFlagDataStore().apply { setString("some_int", "1") }
            manager(store, listOf(ObservableIntDecodingMixin)).valuesOf(IntFeatureFlag).test {
                assertEquals(expected = 1, actual = awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun manager_errors_with_unrecognized_flag_type() {
        runTest {
            // the below IS NOT a `BooleanOption` and therefore will go unrecognized
            val someRandomFlag = object : FeatureFlag<Boolean> {
                override val key: String = "random_key"
                override val default = false
            }

            assertFailsWith<IllegalArgumentException> {
                manager().valuesOf(someRandomFlag).collect { }
            }
        }
    }

    private fun manager(
        store: ObservableFeatureFlagDataStore = InMemoryFeatureFlagDataStore(),
        mixins: List<ObservableFeatureFlagManagerMixin> = emptyList(),
    ) = ObservableMixinFeatureFlagManager(store, mixins)
}