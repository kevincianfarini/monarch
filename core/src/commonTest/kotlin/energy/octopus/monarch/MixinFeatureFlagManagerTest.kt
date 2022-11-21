package energy.octopus.monarch

import kotlinx.coroutines.runBlocking
import kotlin.test.*

class MixinFeatureFlagManagerTest {

    object StringFeature : StringFeatureFlag(key = "foo", default = "blah")
    object BooleanFeature : BooleanFeatureFlag(key = "bool", default = false)
    object DoubleFeature : DoubleFeatureFlag(key = "double", default = 1.5)
    object LongFeature : LongFeatureFlag(key = "long", default = 1027L)
    object ByteArrayFeature : ByteArrayFeatureFlag(key = "byte", default = byteArrayOf(0b1))

    object IntFeatureFlag : FeatureFlag<Int> {
        override val key: String get() = "some_int"
        override val default = -1
    }

    private val intDecodingMixin = object : FeatureFlagManagerMixin {
        override suspend fun <T : Any> currentValueForOrNull(
            flag: FeatureFlag<T>,
            store: FeatureFlagDataStore,
        ): T? = when (flag) {
            is IntFeatureFlag -> {
                val value = store.getString(flag.key)?.toInt()
                (value ?: flag.default) as T
            }
            else -> null
        }
    }


    private val store = object : FeatureFlagDataStore {

        val data: MutableMap<String, Any> = mutableMapOf()

        override suspend fun getBoolean(key: String): Boolean? = data[key] as Boolean?
        override suspend fun getString(key: String): String? = data[key] as String?
        override suspend fun getDouble(key: String): Double? = data[key] as Double?
        override suspend fun getLong(key: String): Long? = data[key] as Long?
        override suspend fun getByteArray(key: String): ByteArray? = data[key] as ByteArray?
    }

    private val manager = MixinFeatureFlagManager(store, listOf(intDecodingMixin))

    @Test fun `manager gets string value`() {
        runBlocking {
            store.data["foo"] = "bar"
            assertEquals(
                expected = "bar",
                actual = manager.currentValueFor(StringFeature),
            )
        }
    }

    @Test fun `manager gets default string value`() {
        runBlocking {
            assertEquals(
                expected = "blah",
                actual = manager.currentValueFor(StringFeature),
            )
        }
    }

    @Test fun `manager gets boolean value`() {
        runBlocking {
            store.data["bool"] = true
            assertTrue(manager.currentValueFor(BooleanFeature))
        }
    }

    @Test fun `manager gets default boolean value`() {
        runBlocking {
            assertFalse(manager.currentValueFor(BooleanFeature))
        }
    }

    @Test fun `manager gets double value`() {
        runBlocking {
            store.data["double"] = 15.7
            assertEquals(
                expected = 15.7,
                actual = manager.currentValueFor(DoubleFeature),
                absoluteTolerance = 0.05,
            )
        }
    }

    @Test fun `manager gets default double value`() {
        runBlocking {
            assertEquals(
                expected = 1.5,
                actual = manager.currentValueFor(DoubleFeature),
                absoluteTolerance = 0.05,
            )
        }
    }

    @Test fun `manager gets long value`() {
        runBlocking {
            store.data["long"] = 27L
            assertEquals(
                expected = 27L,
                actual = manager.currentValueFor(LongFeature),
            )
        }
    }

    @Test fun `manager gets default long value`() {
        runBlocking {
            assertEquals(
                expected = 1027L,
                actual = manager.currentValueFor(LongFeature),
            )
        }
    }

    @Test fun `manager gets byte array value`() {
        runBlocking {
            store.data["byte"] = byteArrayOf(0b11)
            assertContentEquals(
                expected = byteArrayOf(0b11),
                actual = manager.currentValueFor(ByteArrayFeature),
            )
        }
    }

    @Test fun `manager gets default byte array value`() {
        runBlocking {
            assertContentEquals(
                expected = byteArrayOf(0b1),
                actual = manager.currentValueFor(ByteArrayFeature),
            )
        }
    }

    @Test fun `manager gets mixin value`() {
        runBlocking {
            store.data["some_int"] = "1"
            assertEquals(
                expected = 1,
                actual = manager.currentValueFor(IntFeatureFlag),
            )
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
                manager.currentValueFor(someRandomFlag)
            }
        }
    }
}