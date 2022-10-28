package energy.octopus.featureflag

/**
 * A state that a feature flag can assume at a moment in time.
 */
public interface FeatureFlagOption

/**
 * A simple feature which can only be enabled or disabled.
 */
public class BooleanOption(public val isEnabled: Boolean) : FeatureFlagOption

/**
 * A simple feature represented by the backing [value].
 */
public class StringOption(public val value: String) : FeatureFlagOption

/**
 * A simple feature represented by the backing [value].
 */
public class DoubleOption(public val value: Double) : FeatureFlagOption


/**
 * A simple feature represented by the backing [value].
 */
public class LongOption(public val value: Long) : FeatureFlagOption

/**
 * A simple feature represented by the backing [value].
 */
public class ByteArrayOption(public val value: ByteArray) : FeatureFlagOption