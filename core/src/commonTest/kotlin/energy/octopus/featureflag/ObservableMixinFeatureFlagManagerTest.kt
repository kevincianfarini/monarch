package energy.octopus.featureflag

import app.cash.turbine.test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class ObservableMixinFeatureFlagManagerTest {

    object StringFeature : StringFeatureFlag(key = "foo", default = "blah")
    object BooleanFeature : BooleanFeatureFlag(key = "bool", default = false)
    object DoubleFeature : DoubleFeatureFlag(key = "double", default = 1.5)
    object LongFeature : LongFeatureFlag(key = "long", default = 1027L)
    object ByteArrayFeature : ByteArrayFeatureFlag(key = "byte", default = byteArrayOf(0b1))

    object IntFeatureFlag : FeatureFlag<Int> {
        override val key: String get() = "some_int"
        override val default = -1
    }

    // FeatureFlagManager mixin that handles IntFeatureFlags, returns Flow<IntOption>
    private val observableIntDecodingMixin = object : ObservableFeatureFlagManagerMixin {
        override fun <T : Any> valuesOrNull(
            flag: FeatureFlag<T>,
            store: ObservableFeatureFlagDataStore,
        ): Flow<T>? = when (flag) {
            is IntFeatureFlag -> callbackFlow {
                // all values stored as strings in this test
                val onValueChanged: (String?) -> Unit = { str ->
                    trySend((str?.toInt() ?: flag.default) as T)
                }
                val observer = FeatureFlagChangeObserver(onValueChanged)
                store.observeString(flag.key, observer)
            }
            else -> null
        }

        override suspend fun <T : Any> currentValueForOrNull(
            flag: FeatureFlag<T>,
            store: FeatureFlagDataStore
        ): T? = when (flag) {
            is IntFeatureFlag -> {
                (store.getString(flag.key) ?: flag.default ) as T?
            }
            else -> null
        }
    }

    // TODO: extract as FakeObservableFeatureFlagDataStore
    private val store = object : ObservableFeatureFlagDataStore {

        val data: MutableMap<String, Any> = mutableMapOf()

        override suspend fun getBoolean(key: String): Boolean? = data[key] as Boolean?
        override suspend fun getString(key: String): String? = data[key] as String?
        override suspend fun getDouble(key: String): Double? = data[key] as Double?
        override suspend fun getLong(key: String): Long? = data[key] as Long?
        override suspend fun getByteArray(key: String): ByteArray? = data[key] as ByteArray?
        override fun observeString(key: String, observer: FeatureFlagChangeObserver<String?>) {
            observer.onValueChanged(data[key] as String?)
        }
        override fun observeBoolean(key: String, observer: FeatureFlagChangeObserver<Boolean?>) {
            observer.onValueChanged(data[key] as Boolean?)
        }
        override fun observeDouble(key: String, observer: FeatureFlagChangeObserver<Double?>) {
            observer.onValueChanged(data[key] as Double?)
        }
        override fun observeLong(key: String, observer: FeatureFlagChangeObserver<Long?>) {
            observer.onValueChanged(data[key] as Long?)
        }
        override fun observeByteArray(key: String, observer: FeatureFlagChangeObserver<ByteArray?>) {
            observer.onValueChanged(data[key] as ByteArray?)
        }
        override fun <T> removeObserver(key: String, observer: FeatureFlagChangeObserver<T>) {
            TODO("Not yet implemented")
        }
    }

    private val observableManager =
        ObservableMixinFeatureFlagManager(store, listOf(observableIntDecodingMixin))

    @Test
    fun `manager gets string value`() {
        runBlocking {
            store.data["foo"] = "bar"
            observableManager.valuesFor(StringFeature).test {
                assertEquals("bar", awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `manager gets default string value`() {
        runBlocking {
            observableManager.valuesFor(StringFeature).test {
                assertEquals("blah", awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `manager gets boolean value`() {
        runBlocking {
            store.data["bool"] = true
            observableManager.valuesFor(BooleanFeature).test {
                assertTrue(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `manager gets default boolean value`() {
        runBlocking {
            observableManager.valuesFor(BooleanFeature).test {
                assertFalse(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `manager gets double value`() {
        runBlocking {
            store.data["double"] = 15.7
            observableManager.valuesFor(DoubleFeature).test {
                assertEquals(expected = 15.7, actual = awaitItem(), absoluteTolerance = 0.05)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `manager gets default double value`() {
        runBlocking {
            observableManager.valuesFor(DoubleFeature).test {
                assertEquals(expected = 1.5, actual = awaitItem(), absoluteTolerance = 0.05)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `manager gets long value`() {
        runBlocking {
            store.data["long"] = 27L
            observableManager.valuesFor(LongFeature).test {
                assertEquals(expected = 27L, actual = awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `manager gets default long value`() {
        runBlocking {
            observableManager.valuesFor(LongFeature).test {
                assertEquals(expected = 1027L, actual = awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `manager gets byte array value`() {
        runBlocking {
            store.data["byte"] = byteArrayOf(0b11)
            observableManager.valuesFor(ByteArrayFeature).test {
                assertContentEquals(expected = byteArrayOf(0b11), actual = awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `manager gets default byte array value`() {
        runBlocking {
            observableManager.valuesFor(ByteArrayFeature).test {
                assertContentEquals(expected = byteArrayOf(0b1), actual = awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `manager gets mixin value`() {
        runBlocking {
            store.data["some_int"] = "1"
            observableManager.valuesFor(IntFeatureFlag).test {
                assertEquals(expected = 1, actual = awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `manager errors with unrecognized flag type`() {
        runBlocking {
            // the below IS NOT a `BooleanOption` and therefore will go unrecognized
            val someRandomFlag = object : FeatureFlag<Boolean> {
                override val key: String = "random_key"
                override val default = false
            }

            assertFailsWith<IllegalArgumentException> {
                observableManager.valuesFor(someRandomFlag)
            }
        }
    }
}


