package io.github.kevincianfarini.monarch

import app.cash.turbine.test
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class ObservableMixinFeatureFlagManagerTest {

    @Test fun `manager gets string value`() {
        runBlocking {
            val store = InMemoryFeatureFlagDataStore().apply { setValue("foo", "bar") }
            manager(store).valuesOf(StringFeature).test {
                assertEquals("bar", awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager gets default string value`() {
        runBlocking {
            manager().valuesOf(StringFeature).test {
                assertEquals("blah", awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager gets boolean value`() {
        runBlocking {
            val store = InMemoryFeatureFlagDataStore().apply { setValue("bool", true) }
            manager(store).valuesOf(BooleanFeature).test {
                assertTrue(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager gets default boolean value`() {
        runBlocking {
            manager().valuesOf(BooleanFeature).test {
                assertFalse(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager gets double value`() {
        runBlocking {
            val store = InMemoryFeatureFlagDataStore().apply { setValue("double", 15.7) }
            manager(store).valuesOf(DoubleFeature).test {
                assertEquals(expected = 15.7, actual = awaitItem(), absoluteTolerance = 0.05)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager gets default double value`() {
        runBlocking {
            manager().valuesOf(DoubleFeature).test {
                assertEquals(expected = 1.5, actual = awaitItem(), absoluteTolerance = 0.05)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager gets long value`() {
        runBlocking {
            val store = InMemoryFeatureFlagDataStore().apply { setValue("long", 27L) }
            manager(store).valuesOf(LongFeature).test {
                assertEquals(expected = 27L, actual = awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager gets default long value`() {
        runBlocking {
            manager().valuesOf(LongFeature).test {
                assertEquals(expected = 1027L, actual = awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager gets mixin value`() {
        runBlocking {
            val store = InMemoryFeatureFlagDataStore().apply { setValue("some_int", "1") }
            manager(store, listOf(ObservableIntDecodingMixin)).valuesOf(IntFeatureFlag).test {
                assertEquals(expected = 1, actual = awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager errors with unrecognized flag type`() {
        runBlocking {
            // the below IS NOT a `BooleanOption` and therefore will go unrecognized
            val someRandomFlag = object : FeatureFlag<Boolean> {
                override val key: String = "random_key"
                override val default = false
            }

            assertFailsWith<IllegalArgumentException> {
                manager().valuesOf(someRandomFlag)
            }
        }
    }

    private fun manager(
        store: ObservableFeatureFlagDataStore = InMemoryFeatureFlagDataStore(),
        mixins: List<ObservableFeatureFlagManagerMixin> = emptyList(),
    ) = ObservableMixinFeatureFlagManager(store, mixins)
}
