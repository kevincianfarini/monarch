package io.github.kevincianfarini.monarch.environment

import kotlin.test.*

class EnvironmentVariableFeatureFlagDataStoreTest {

    @Test
    fun strictly_typed_boolean_returns_value() {
        val store = EnvironmentVariableFeatureFlagDataStore(strictlyTyped = true) { "true" }
        assertTrue(store.getBoolean(key = "key", default = false))
    }

    @Test
    fun strictly_typed_boolean_fails() {
        val store = EnvironmentVariableFeatureFlagDataStore(strictlyTyped = true) { "tRuE" }
        assertFailsWith<IllegalArgumentException> {
            store.getBoolean(key = "key", default = false)
        }
    }

    @Test
    fun loosely_typed_boolean_returns_default_on_failure() {
        val store = EnvironmentVariableFeatureFlagDataStore(strictlyTyped = false) { "tRuE" }
        assertFalse(store.getBoolean(key = "key", default = false))
    }

    @Test
    fun no_underlying_value_boolean_returns_default() {
        val store = EnvironmentVariableFeatureFlagDataStore(strictlyTyped = false) { null }
        assertFalse(store.getBoolean(key = "key", default = false))
    }

    @Test
    fun string_returns_environment_variable() {
        val store = EnvironmentVariableFeatureFlagDataStore(strictlyTyped = true) { "some string" }
        assertEquals("some string", store.getString(key = "key", default = "default"))
    }

    @Test
    fun string_returns_default_when_no_environment_variable() {
        val store = EnvironmentVariableFeatureFlagDataStore(strictlyTyped = true) { null }
        assertEquals("default", store.getString(key = "key", default = "default"))
    }

    @Test
    fun strictly_typed_double_returns_value() {
        val store = EnvironmentVariableFeatureFlagDataStore(strictlyTyped = true) { "1.2" }
        assertEquals(1.2, store.getDouble(key = "key", default = 0.0))
    }

    @Test
    fun strictly_typed_double_fails() {
        val store = EnvironmentVariableFeatureFlagDataStore(strictlyTyped = true) { "Not a number" }
        assertFailsWith<NumberFormatException> {
            store.getDouble(key = "key", default = 0.0)
        }
    }

    @Test
    fun loosely_typed_double_returns_default_on_failure() {
        val store = EnvironmentVariableFeatureFlagDataStore(strictlyTyped = false) { "Not a number" }
        assertEquals(0.0, store.getDouble(key = "key", default = 0.0))
    }

    @Test
    fun no_underlying_value_double_returns_default() {
        val store = EnvironmentVariableFeatureFlagDataStore(strictlyTyped = true) { null }
        assertEquals(0.0, store.getDouble(key = "key", default = 0.0))
    }

    @Test
    fun strictly_typed_long_returns_value() {
        val store = EnvironmentVariableFeatureFlagDataStore(strictlyTyped = true) { "1000" }
        assertEquals(1000, store.getLong(key = "key", default = 0))
    }

    @Test
    fun strictly_typed_long_fails() {
        val store = EnvironmentVariableFeatureFlagDataStore(strictlyTyped = true) { "Not a number" }
        assertFailsWith<NumberFormatException> {
            store.getLong(key = "key", default = 0)
        }
    }

    @Test
    fun loosely_typed_long_returns_default_on_failure() {
        val store = EnvironmentVariableFeatureFlagDataStore(strictlyTyped = false) { "Not a number" }
        assertEquals(0, store.getLong(key = "key", default = 0))
    }

    @Test
    fun no_underlying_value_long_returns_default() {
        val store = EnvironmentVariableFeatureFlagDataStore(strictlyTyped = true) { null }
        assertEquals(0, store.getLong(key = "key", default = 0))
    }

    @Test
    fun strictly_typed_byte_array_returns_value() {
        val store = EnvironmentVariableFeatureFlagDataStore(strictlyTyped = true) { "0f00" }
        assertContentEquals(
            byteArrayOf(0x0f, 0x00),
            store.getByteArray(key = "key", default = byteArrayOf())
        )
    }

    @Test
    fun strictly_typed_byte_array_fails() {
        val store = EnvironmentVariableFeatureFlagDataStore(strictlyTyped = true) { "0f0" }
        assertFailsWith<IllegalArgumentException> {
            store.getByteArray(key = "key", default = byteArrayOf())
        }
    }

    @Test
    fun loosely_typed_byte_array_returns_default_on_failure() {
        val store = EnvironmentVariableFeatureFlagDataStore(strictlyTyped = false) { "0f0" }
        assertContentEquals(
            byteArrayOf(),
            store.getByteArray(key = "key", default = byteArrayOf())
        )
    }

    @Test
    fun no_underlying_value_byte_array_returns_default() {
        val store = EnvironmentVariableFeatureFlagDataStore(strictlyTyped = true) { null }
        assertContentEquals(
            byteArrayOf(),
            store.getByteArray(key = "key", default = byteArrayOf())
        )
    }
}