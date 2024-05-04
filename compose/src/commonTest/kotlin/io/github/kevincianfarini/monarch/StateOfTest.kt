package io.github.kevincianfarini.monarch

import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StateOfTest {

    @Test
    fun stateOf_starts_with_current_value() = runTest {
        val manager = InMemoryFeatureFlagManager().apply {
            setCurrentValueOf(SomeBooleanFlag, true)
        }
        val flow = moleculeFlow(RecompositionMode.Immediate) {
            manager.stateOf(SomeBooleanFlag).value
        }
        flow.test { assertTrue(awaitItem()) }
    }

    @Test
    fun stateOf_updates_values() = runTest {
        val manager = InMemoryFeatureFlagManager()
        val flow = moleculeFlow(RecompositionMode.Immediate) {
            manager.stateOf(SomeBooleanFlag).value
        }
        flow.test {
            assertFalse(awaitItem())
            manager.setCurrentValueOf(SomeBooleanFlag, true)
            assertTrue(awaitItem())
        }
    }
}

private object SomeBooleanFlag : BooleanFeatureFlag("blah", false)