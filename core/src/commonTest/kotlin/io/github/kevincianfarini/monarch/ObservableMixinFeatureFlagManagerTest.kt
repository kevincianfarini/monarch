package io.github.kevincianfarini.monarch

import app.cash.turbine.test
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class ObservableMixinFeatureFlagManagerTest {

    @Test fun `manager gets string value`() {
        runBlocking {
            val store = FakeFeatureFlagDataStore().apply { setValue("foo", "bar") }
            manager(store).valuesFor(StringFeature).test {
                assertEquals("bar", awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager gets default string value`() {
        runBlocking {
            manager().valuesFor(StringFeature).test {
                assertEquals("blah", awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager gets boolean value`() {
        runBlocking {
            val store = FakeFeatureFlagDataStore().apply { setValue("bool", true) }
            manager(store).valuesFor(BooleanFeature).test {
                assertTrue(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager gets default boolean value`() {
        runBlocking {
            manager().valuesFor(BooleanFeature).test {
                assertFalse(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager gets double value`() {
        runBlocking {
            val store = FakeFeatureFlagDataStore().apply { setValue("double", 15.7) }
            manager(store).valuesFor(DoubleFeature).test {
                assertEquals(expected = 15.7, actual = awaitItem(), absoluteTolerance = 0.05)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager gets default double value`() {
        runBlocking {
            manager().valuesFor(DoubleFeature).test {
                assertEquals(expected = 1.5, actual = awaitItem(), absoluteTolerance = 0.05)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager gets long value`() {
        runBlocking {
            val store = FakeFeatureFlagDataStore().apply { setValue("long", 27L) }
            manager(store).valuesFor(LongFeature).test {
                assertEquals(expected = 27L, actual = awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager gets default long value`() {
        runBlocking {
            manager().valuesFor(LongFeature).test {
                assertEquals(expected = 1027L, actual = awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager gets byte array value`() {
        runBlocking {
            val store = FakeFeatureFlagDataStore().apply { setValue("byte", byteArrayOf(0b11)) }
            manager(store).valuesFor(ByteArrayFeature).test {
                assertContentEquals(expected = byteArrayOf(0b11), actual = awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager gets default byte array value`() {
        runBlocking {
            manager().valuesFor(ByteArrayFeature).test {
                assertContentEquals(expected = byteArrayOf(0b1), actual = awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test fun `manager gets mixin value`() {
        runBlocking {
            val store = FakeFeatureFlagDataStore().apply { setValue("some_int", "1") }
            manager(store, listOf(ObservableIntDecodingMixin)).valuesFor(IntFeatureFlag).test {
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
                manager().valuesFor(someRandomFlag)
            }
        }
    }

    private fun manager(
        store: ObservableFeatureFlagDataStore = FakeFeatureFlagDataStore(),
        mixins: List<ObservableFeatureFlagManagerMixin> = emptyList(),
    ) = ObservableMixinFeatureFlagManager(store, mixins)
}