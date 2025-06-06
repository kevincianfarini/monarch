package io.github.kevincianfarini.monarch

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class InMemoryFeatureFlagDataStoreOverrideTest {

    @Test fun store_cache_overrides_delegate_synchronous() {
        listOf<Triple<Any, Any, InMemoryFeatureFlagDataStoreOverride.(String) -> Any?>>(
            Triple(true, false) { getBoolean(it, false) },
            Triple("correct", "incorrect") { getString(it, "incorrect") },
            Triple(1.5, 3.0) { getDouble(it, 3.0) },
            Triple(1L, 2L) { getLong(it, 2L) },
        ).forEach { (overrideValue, delegateValue, produceFn) ->
            testCacheOverridesDelegateSynchronousParameterized(overrideValue, delegateValue, produceFn)
        }
    }

    private fun testCacheOverridesDelegateSynchronousParameterized(
        overrideValue: Any,
        delegateValue: Any,
        produceValue: InMemoryFeatureFlagDataStoreOverride.(String) -> Any?
    ) {
        val key = "foo"
        val delegate = InMemoryFeatureFlagDataStore().apply {
            when (delegateValue) {
                is String -> setString(key, delegateValue)
                is Boolean -> setBoolean(key, delegateValue)
                is Long -> setLong(key, delegateValue)
                is Double -> setDouble(key, delegateValue)
                else -> error("Invalid value.")
            }
        }
        val storeOverride = InMemoryFeatureFlagDataStoreOverride(delegate = delegate).apply {
            when (overrideValue) {
                is String -> setString(key, overrideValue)
                is Boolean -> setBoolean(key, overrideValue)
                is Long -> setLong(key, overrideValue)
                is Double -> setDouble(key, overrideValue)
                else -> error("Invalid value.")
            }
        }

        assertEquals(
            expected = overrideValue,
            actual = storeOverride.produceValue(key),
        )
    }

    @Test fun store_cache_falls_back_to_delegate_synchronous() {
        listOf<Pair<Any, InMemoryFeatureFlagDataStoreOverride.(String) -> Any?>>(
            Pair(false) { getBoolean(it, true) },
            Pair("correct") { getString(it, "incorrect") },
            Pair(3.0) { getDouble(it, 1.5) },
            Pair(2L) { getLong(it, 1L) },
        ).forEach { (delegateValue, produceFn) ->
            testCacheFallsBackToDelegateSynchronousParameterized(delegateValue, produceFn)
        }
    }

    private fun testCacheFallsBackToDelegateSynchronousParameterized(
        delegateValue: Any,
        produceValue: InMemoryFeatureFlagDataStoreOverride.(String) -> Any?
    ) {
        val key = "foo"
        val delegate = InMemoryFeatureFlagDataStore().apply {
            when (delegateValue) {
                is String -> setString(key, delegateValue)
                is Boolean -> setBoolean(key, delegateValue)
                is Long -> setLong(key, delegateValue)
                is Double -> setDouble(key, delegateValue)
                else -> error("Invalid value.")
            }
        }
        val storeOverride = InMemoryFeatureFlagDataStoreOverride(delegate = delegate)

        assertEquals(
            expected = delegateValue,
            actual = storeOverride.produceValue(key),
        )
    }

    @Test fun store_cache_overrides_delegate_flow() {
        listOf<Triple<Any, Any, InMemoryFeatureFlagDataStoreOverride.(String) -> Flow<*>>>(
            Triple(true, false) { observeBoolean(it, false) },
            Triple("correct", "incorrect") { observeString(it, "incorrect") },
            Triple(1.5, 3.0) { observeDouble(it, 3.0) },
            Triple(1L, 2L) { observeLong(it, 2L) },
        ).forEach { (overrideValue, delegateValue, produceFn) ->
            storeCacheOverridesDelegateFlowParameterized(overrideValue, delegateValue, produceFn)
        }
    }

    private fun storeCacheOverridesDelegateFlowParameterized(
        overrideValue: Any,
        delegateValue: Any,
        produceFlow: InMemoryFeatureFlagDataStoreOverride.(String) -> Flow<*>
    ) = runTest {
        val key = "foo"
        val delegate = InMemoryFeatureFlagDataStore().apply {
            when (delegateValue) {
                is String -> setString(key, delegateValue)
                is Boolean -> setBoolean(key, delegateValue)
                is Long -> setLong(key, delegateValue)
                is Double -> setDouble(key, delegateValue)
                else -> error("Invalid value.")
            }
        }
        val storeOverride = InMemoryFeatureFlagDataStoreOverride(delegate).apply {
            when (overrideValue) {
                is String -> setString(key, overrideValue)
                is Boolean -> setBoolean(key, overrideValue)
                is Long -> setLong(key, overrideValue)
                is Double -> setDouble(key, overrideValue)
                else -> error("Invalid value.")
            }
        }

        storeOverride.produceFlow(key).test {
            assertEquals(
                expected = overrideValue,
                actual = awaitItem(),
            )
        }
    }

    @Test fun store_cache_falls_back_to_delegate_flow() {
        listOf<Pair<Any, InMemoryFeatureFlagDataStoreOverride.(String) -> Flow<*>>>(
            Pair(false) { observeBoolean(it, true) },
            Pair("correct") { observeString(it, "incorrect") },
            Pair(3.0) { observeDouble(it, 1.5) },
            Pair(2L) { observeLong(it, 1L) },
        ).forEach { (delegateValue, produceFn) ->
            storeCacheFallsBackToDelegateFlowParameterized(delegateValue, produceFn)
        }
    }

    private fun storeCacheFallsBackToDelegateFlowParameterized(
        delegateValue: Any,
        produceFlow: InMemoryFeatureFlagDataStoreOverride.(String) -> Flow<*>
    ) = runTest {
        val key = "foo"
        val delegate = InMemoryFeatureFlagDataStore().apply {
            when (delegateValue) {
                is String -> setString(key, delegateValue)
                is Boolean -> setBoolean(key, delegateValue)
                is Long -> setLong(key, delegateValue)
                is Double -> setDouble(key, delegateValue)
                else -> error("Invalid value.")
            }
        }
        val storeOverride = InMemoryFeatureFlagDataStoreOverride(delegate = delegate)

        storeOverride.produceFlow(key).test {
            assertEquals(
                expected = delegateValue,
                actual = awaitItem(),
            )
        }
    }

    @Test fun writing_to_store_cache_emits_new_value_in_active_flows() {
        listOf<Triple<Any, InMemoryFeatureFlagDataStoreOverride.(String) -> Unit, InMemoryFeatureFlagDataStoreOverride.(String) -> Flow<*>>>(
            Triple(true, { setBoolean(it, false) }) { observeBoolean(it, true) },
            Triple("correct", { setString(it, "also correct") }) { observeString(it, "incorrect") },
            Triple(1.5, { setDouble(it, 3.0) }) { observeDouble(it, 4.5) },
            Triple(1L, { setLong(it, 3L) }) { observeLong(it, 2L) },
        ).forEach { (initialValue, newValue, produceFn) ->
            writingToStoreCacheEmitsNewValueParameterized(initialValue, newValue, produceFn)
        }
    }

    private fun writingToStoreCacheEmitsNewValueParameterized(
        initialValue: Any,
        setNewValue: InMemoryFeatureFlagDataStoreOverride.(String) -> Unit,
        produceFlow: InMemoryFeatureFlagDataStoreOverride.(String) -> Flow<*>
    ) = runTest {
        val key = "foo"
        val storeOverride = InMemoryFeatureFlagDataStoreOverride(InMemoryFeatureFlagDataStore()).apply {
            when (initialValue) {
                is String -> setString(key, initialValue)
                is Boolean -> setBoolean(key, initialValue)
                is Long -> setLong(key, initialValue)
                is Double -> setDouble(key, initialValue)
                else -> error("Invalid value.")
            }
        }
        storeOverride.produceFlow(key).test {
            assertEquals(
                expected = initialValue,
                actual = awaitItem(),
            )
            storeOverride.setNewValue(key)
            assertNotEquals(illegal = initialValue, actual = awaitItem())
        }
    }
}