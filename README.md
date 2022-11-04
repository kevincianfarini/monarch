# Monarch 🦋

Monarch is a small, flexible, type safe, and multiplatform abstraction for feature flags.  

> In chaos theory, the butterfly effect is the sensitive dependence on initial conditions in which a small change in one state of a deterministic nonlinear system can result in large differences in a later state.
> 
> — [Wikipedia](https://en.wikipedia.org/wiki/Butterfly_effect)

## Download

TODO

## Usage 

### Defining flags

Monarch provides compile time safety for the feature flags you define and consume.
Flag keys are bound to a type and a default value. 

```kt
object FancyFeatureEnabled : BooleanFeatureFlag(
    key = "fancy_feature_enabled",
    default = false,
)
```

The `FancyFeatureEnabled` flag can later be referenced in code and checked by the compiler. 

Monarch provides several feature flag types in the 'core' artifact. 

* `BooleanFeatureFlag`
* `LongFeatureFlag`
* `DoubleFeatureFlag`
* `StringFeatureFlag`
* `ByteArrayFeatureFlag`

### Obtaining values

Values can be obtained from a `FeatureFlagManager` for a given feature flag.
These are suspending operations. 

```kt
object FancyFeatureEnabled : BooleanFeatureFlag(/* omitted */)

suspend fun main() {
    val manager: FeatureFlagManager = TODO("See below.") 
    if (manager.currentValueFor(FancyFeatureEnabled)) {
        showFancyFeature()
    } else {
        showBoringFeature()
    }
}
```

### Extending with mixins

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
        is JsonFeatureFlag<T> -> {
            val value = store.getString(flag.key)?.let { flag.deserialize(it, json) }
            (value ?: flag.default)
        }
        else -> null // This mixin doesn't handle this flag.
    }
}
```

The above sample mixin implementation will handle _all_ instances of `JsonFeatureFlag`, and therefore both 
`OneValueFeatureFlag` and `TwoValueFeatureFlag` would be handled. 

### Constructing a FeatureFlagManager

Monarch's built-in implementations of `FeatureFlagManager` take lists of
`FeatureFlagManagerMixin` and a `FeatureFlagDataStore`.

A `FeatureFlagDataStore` is the entity closely tied to the underlying feature flagging SDK. 
It's unlikely you will implement this unless you're integrating with a feature flagging platform 
that Monarch doesn't currently support. 

Typical usage of `FeatureFlagManager` implementations expects that the `FeatureFlagDataStore`, 
all `FeatureFlagManagerMixin` instances, and the `FeatureFlagManager` itself will be provided
as part of your dependency graph. Below is a sample with Dagger. 

```kt
@Module object FeatureFlaggingModule {
    
    @Provides fun providesDataStore(): FeatureFlagDataStore { /* omitted */ }
    
    @Provides @IntoSet fun providesJsonMixin(
        json: Json
    ): FeatureFlagManagerMixin = JsonFeatureFlagManagerMixin(json)
    
    @Provides fun providesManager(
        dataStore: FeatureFlagDataStore, 
        mixins: Set<FeatureFlagManagerMixin>,
    ): FeatureFlagManager = MixinFeatureFlagManager(
        store = dataStore, 
        mixins = mixins.toList(),
    )
}
```

### Observing value changes

Some third party SDKs, like LaunchDarkly, provide callbacks for flags when their values change. 
Monarch provides an additional abstraction for this, called `ObservableFeatureFlagManager`, 
which exposes these changes as a `Flow`. 

```kt
object FancyFeatureEnabled : BooleanFeatureFlag(/* omitted */)

suspend fun main() {
    val manager: ObservableFeatureFlagManager = TODO()
    manager.valuesFor(FancyFeatureEnabled).collect { enabled -> 
        if (enabled) {
            showFancyFeature()
        } else {
            showBoringFeature()
        }
    }
}
```

## License 

```
MIT License

Copyright (c) 2019 Octopus Energy Limited.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```