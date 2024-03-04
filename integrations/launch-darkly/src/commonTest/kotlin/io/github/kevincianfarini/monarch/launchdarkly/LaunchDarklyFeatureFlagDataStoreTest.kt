package io.github.kevincianfarini.monarch.launchdarkly

import app.cash.turbine.test
import io.github.kevincianfarini.monarch.ObservableFeatureFlagDataStore
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.*

class LaunchDarklyFeatureFlagDataStoreTest {

    @Test fun unpopulated_boolean_returns_default() {
        val (dataStore, _) = sut()
        assertFalse(dataStore.getBoolean("key", false))
    }

    @Test fun populated_boolean_returns_value() {
        val (dataStore, mutate) = sut()
        mutate.setVariation("key", true)
        assertTrue(dataStore.getBoolean("key", false))
    }

    @Test fun unpopulated_double_returns_default() {
        val (dataStore, _) = sut()
        assertEquals(0.0, dataStore.getDouble("key", 0.0))
    }

    @Test fun populated_double_returns_value() {
        val (dataStore, mutate) = sut()
        mutate.setVariation("key", 4.1)
        assertEquals(4.1, dataStore.getDouble("key", 0.0))
    }

    @Test fun unpopulated_long_returns_default() {
        val (dataStore, _) = sut()
        assertEquals(0L, dataStore.getLong("key", 0L))
    }

    @Test fun populated_long_returns_value() {
        val (dataStore, mutate) = sut()
        mutate.setVariation("key", 4)
        assertEquals(4, dataStore.getLong("key", 0L))
    }

    @Test fun unpopulated_string_returns_default() {
        val (dataStore, _) = sut()
        assertEquals("default", dataStore.getString("key", "default"))
    }

    @Test fun populated_string_returns_value() {
        val (dataStore, mutate) = sut()
        mutate.setVariation("key", "non_default")
        assertEquals("non_default", dataStore.getString("key", "default"))
    }

    @Test fun getting_byte_array_errors() = runTest {
        val (dataStore, _) = sut()
        assertFailsWith<NotImplementedError> {
            dataStore.getByteArray("key", byteArrayOf())
        }
    }

    @Test fun getting_string_from_json_flag_returns_value() {
        val (dataStore, mutate) = sut()
        val expected = Thing(1, 2)
        mutate.setVariation("key", expected, Thing.serializer())
        val jsonString = dataStore.getString("key", "{}")
        assertEquals(expected, Json.Default.decodeFromString(Thing.serializer(), jsonString))
    }

    @Test fun observing_string_coerces_initial_default_to_null() = runTest {
        val (dataStore, _) = sut()
        dataStore.observeString("key", "default").test {
            assertEquals("default", awaitItem())
        }
    }

    @Test fun observing_string_emits_non_default_current_value() = runTest {
        val (dataStore, mutate) = sut()
        mutate.setVariation("key", "non_default")
        dataStore.observeString("key", "default").test {
            assertEquals("non_default", awaitItem())
        }
    }

    @Test fun observing_string_emits_value_updates() = runTest {
        val (dataStore, mutate) = sut()
        dataStore.observeString("key", "default").test {
            assertEquals("default", awaitItem())
            mutate.setVariation("key", "non_default")
            assertEquals("non_default", awaitItem())
        }
    }

    @Test fun observing_boolean_coerces_initial_default_to_null() = runTest {
        val (dataStore, _) = sut()
        dataStore.observeBoolean("key", false).test {
            assertFalse(awaitItem())
        }
    }

    @Test fun observing_boolean_emits_non_default_current_value() = runTest {
        val (dataStore, mutate) = sut()
        mutate.setVariation("key", true)
        dataStore.observeBoolean("key", false).test {
            assertTrue(awaitItem())
        }
    }

    @Test fun observing_boolean_emits_value_updates() = runTest {
        val (dataStore, mutate) = sut()
        dataStore.observeBoolean("key", false).test {
            assertFalse(awaitItem())
            mutate.setVariation("key", true)
            assertTrue(awaitItem())
        }
    }

    @Test fun observing_double_coerces_initial_default_to_null() = runTest {
        val (dataStore, _) = sut()
        dataStore.observeDouble("key", -1.8).test {
            assertEquals(-1.8, awaitItem())
        }
    }

    @Test fun observing_double_emits_non_default_current_value() = runTest {
        val (dataStore, mutate) = sut()
        mutate.setVariation("key", 3.5)
        dataStore.observeDouble("key", -1.8).test {
            assertEquals(3.5, awaitItem())
        }
    }

    @Test fun observing_double_emits_value_updates() = runTest {
        val (dataStore, mutate) = sut()
        dataStore.observeDouble("key", -1.8).test {
            assertEquals(-1.8, awaitItem())
            mutate.setVariation("key", 3.5)
            assertEquals(3.5, awaitItem())
        }
    }

    @Test fun observing_long_coerces_initial_default_to_null() = runTest {
        val (dataStore, _) = sut()
        dataStore.observeLong("key", -1L).test {
            assertEquals(-1L, awaitItem())
        }
    }

    @Test fun observing_long_emits_non_default_current_value() = runTest {
        val (dataStore, mutate) = sut()
        mutate.setVariation("key", 3)
        dataStore.observeLong("key", -1L).test {
            assertEquals(3L, awaitItem())
        }
    }

    @Test fun observing_long_emits_value_updates() = runTest {
        val (dataStore, mutate) = sut()
        dataStore.observeLong("key", -1L).test {
            assertEquals(-1L, awaitItem())
            mutate.setVariation("key", 3)
            assertEquals(3L, awaitItem())
        }
    }

    @Test fun observing_byte_array_errors() = runTest {
        val (dataStore, _) = sut()
        assertFailsWith<NotImplementedError> {
            dataStore.observeByteArray("key", byteArrayOf())
        }
    }

    @Test fun observing_json_string_emits_value_updates() = runTest {
        val (dataStore, mutate) = sut()
        val first = Thing(1, 2)
        val second = Thing(2, 3)
        mutate.setVariation("key", first, Thing.serializer())
        dataStore.observeString("key", "{}").test {
            val item = awaitItem()
            assertEquals(first, Json.Default.decodeFromString(Thing.serializer(), item))
            mutate.setVariation("key", second, Thing.serializer())
            assertEquals(second, Json.Default.decodeFromString(Thing.serializer(), awaitItem()))
        }
    }
}

@Serializable
private data class Thing(val first: Int, val second: Int)

expect fun sut(): Pair<ObservableFeatureFlagDataStore, MutableLDClientInterface>
