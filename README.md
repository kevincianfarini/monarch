# Monarch 🦋

Monarch is a small, flexible, type safe, and multiplatform abstraction for feature flags.  

> In chaos theory, the butterfly effect is the sensitive dependence on initial conditions in which a small change in one state of a deterministic nonlinear system can result in large differences in a later state.
> 
> — [Wikipedia](https://en.wikipedia.org/wiki/Butterfly_effect)

## Download

```toml
[versions]
monarch = "0.2.2"

[libraries]
monarch-compose = { module = "io.github.kevincianfarini.monarch:compose", version.ref = "monarch" }
monarch-core = { module = "io.github.kevincianfarini.monarch:core", version.ref = "monarch" }
monarch-integration-environment = { module = "io.github.kevincianfarini.monarch:environment-integration", version.ref = "monarch" }
monarch-integration-launchdarkly = { module = "io.github.kevincianfarini.monarch:launch-darkly-integration", version.ref = "monarch" }
monarch-mixin-kotlinxjson = { module = "io.github.kevincianfarini.monarch:kotlinx-serialization-mixin", version.ref = "monarch" }
monarch-test = { module = "io.github.kevincianfarini.monarch:test", version.ref = "monarch" }
```

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

### Obtaining values

Values can be obtained from a `FeatureFlagManager` for a given feature flag.

```kt
fun showFeature(manager: FeatureFlagManager) {
    if (manager.currentValueOf(FancyFeatureEnabled)) {
        showFancyFeature()
    } else {
        showBoringFeature()
    }
}
```

### Observing value changes as a Flow

Some third party SDKs, like LaunchDarkly, provide callbacks for flags when their values change.
Monarch provides an additional abstraction for this, called `ObservableFeatureFlagManager`,
which exposes these changes as a `Flow`.

```kt
suspend fun showFeature(manager: ObservableFeatureFlagManager) {
    manager.valuesOf(FancyFeatureEnabled).collect { enabled -> 
        if (enabled) {
            showFancyFeature()
        } else {
            showBoringFeature()
        }
    }
}
```

### Observing value changes as a Compose State

Monarch offers a companion artifact that makes observing flag values as Compose State simple.

```kotlin
@Composable 
fun ShowFeature(manager: ObservableFeatureFlagManager) {
    val enabled by manager.stateOf(FancyFeatureEnabled)
    if (enabled) {
        FancyFeature()
    } else {
        BoringFeature()
    }
}
```

### Testing

Monarch provides an out-of-the-box test implementation of an `ObservableFeatureFlagManager` called `InMemoryFeatureFlagManager`.
It can be mutated under test to exercise specific branches of code dictated by your flags.

```kotlin
@Test 
fun test_flag_changes() = runTest {
   val manager = InMemoryFeatureFlagManager()
   manager.valuesOf(FancyFeatureEnabled).test {
       assertFalse(awaitItem())
       manager.setCurrentValueOf(FancyFeatureEnabled, true)
       assertTrue(awaitItem())
   }
}
```

### Supplying a FeatureFlagManager

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
    
    @Provides 
    fun providesDataStore(): FeatureFlagDataStore { /* omitted */ }
    
    @Provides @IntoSet 
    fun providesJsonMixin(
        json: Json
    ): FeatureFlagManagerMixin = JsonFeatureFlagManagerMixin(json)
    
    @Provides 
    fun providesManager(
        dataStore: FeatureFlagDataStore, 
        mixins: Set<FeatureFlagManagerMixin>,
    ): FeatureFlagManager = MixinFeatureFlagManager(
        store = dataStore, 
        mixins = mixins.toList(),
    )
}
```