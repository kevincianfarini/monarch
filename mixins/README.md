# Extending with mixins

The core flag types may not sufficiently cover your needs. Custom flag types,
like ones encoded with JSON, are common in complex programs. Define custom flags
by implementing the `FeatureFlag` interface.

```kt
abstract class JsonFeatureFlag<EncodedType : Any>(
    override val key: String,
    override val default: EncodedType,
) : FeatureFlag<EncodedType> {
    
    abstract fun deserialize(raw: String, json: Json): EncodedType
}

@Serializable class OneValue(val value: Int)
@Serializable class TwoValue(val firstValue: Int, val secondValue: Int)

object OneValueFlag : JsonFeatureFlag(
    key = "one_value",
    default = OneValue(1),
) { /* deserialization omitted */ }

object TwoValueFlag : JsonFeatureFlag(
    key = "two_value",
    default = TwoValue(1, 2),
) { /* deserialization omitted */ }
```

When used in conjunction with a `FeatureFlagManagerMixin`, Monarch can be extended to support
any arbitrary feature flag format. `FeatureFlagManagerMixin` instances should return a value when invoked if it handles a given
`FeatureFlag` type, otherwise it should return `null`. A `FeatureFlagManager` will
exhaustively check all of its mixins until the requested flag is handled.

```kt
class JsonFeatureFlagManagerMixin(private val json: Json) : FeatureFlagManagerMixin {

    override suspend fun <T : Any> currentValueForOrNull(
        flag: FeatureFlag<T>,
        store: FeatureFlagDataStore,
    ): T? = when (flag) {
        is JsonFeatureFlag<T> -> store.getString(flag.key, flag.serializedDefault(json)).let { string ->
            flag.deserialize(string, json)
        }
        else -> null // This mixin doesn't handle this flag.
    }
}
```

The above sample mixin implementation will handle _all_ instances of `JsonFeatureFlag`, and therefore both
`OneValueFeatureFlag` and `TwoValueFeatureFlag` would be handled. 

Observing custom implementations of `FeatureFlag` requires implementing `ObservableFeatureFlagManagerMixin`.

```kt
public class ObservableJsonFeatureFlagManagerMixin(
    private val json: Json,
) : ObservableFeatureFlagManagerMixin {
    
    public override fun <T : Any> currentValueOrNull(
        flag: FeatureFlag<T>,
        store: ObservableFeatureFlagDataStore
    ): T? { /* omitted */ }

    public override fun <T : FeatureFlagOption> valuesOrNull(
        flag: FeatureFlag<T>,
        store: ObservableFeatureFlagDataStore
    ): Flow<T>? = when (flag) {
        is JsonFeatureFlag<T> -> store.observeString(flag.key, flag.serializedDefault(json)).map { string ->
            flag.deserialize(string, json)
        }
        else -> null
    }
}
```
