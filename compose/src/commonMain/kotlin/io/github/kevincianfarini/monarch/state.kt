package io.github.kevincianfarini.monarch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember

/**
 * Acquire a [State] of [flag] that updates according to the underlying flag's value. The initial value of the returned
 * state will be the flag's current value.
 */
@Composable
public fun <T : Any> ObservableFeatureFlagManager.stateOf(flag: FeatureFlag<T>): State<T> {
    return remember(this, flag) { valuesOf(flag) }.collectAsState(currentValueOf(flag))
}