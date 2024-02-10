package io.github.kevincianfarini.monarch

import app.cash.turbine.test
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class FakeFeatureFlagManagerTest {

    @Test fun `returns default value`() {
        runBlocking {
            assertEquals(
                expected = SomeFlag.default,
                actual = FakeFeatureFlagManager().currentValueFor(SomeFlag)
            )
        }
    }

    @Test fun `returns explicitly set value`() {
        runBlocking {
            val manager = FakeFeatureFlagManager().apply {
                setCurrentValueFor(SomeFlag, 1L)
            }
            assertEquals(
                expected = 1L,
                actual = manager.currentValueFor(SomeFlag)
            )
        }
    }

    @Test fun `observing returns default value`() {
        runBlocking {
            assertEquals(
                expected = SomeFlag.default,
                actual = FakeFeatureFlagManager()
                    .valuesFor(SomeFlag)
                    .first(),
            )
        }
    }

    @Test fun `observing emits updates to flags`() {
        runBlocking {
            val manager = FakeFeatureFlagManager()
            manager.valuesFor(SomeFlag).test {
                assertEquals(
                    expected = SomeFlag.default,
                    actual = awaitItem(),
                )
                manager.setCurrentValueFor(SomeFlag, 1L)
                assertEquals(
                    expected = 1L,
                    actual = awaitItem(),
                )
                expectNoEvents()
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}

private object SomeFlag : LongFeatureFlag(
    key = "some_flag",
    default = -1L,
)
