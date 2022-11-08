package energy.octopus.featureflag

public interface FeatureFlag<OptionType : Any> {

    /**
     * The value that's used to extract this feature flag value in the underlying storage mechanism.
     */
    public val key: String

    /**
     * The default option to be assumed when no backing value is present.
     */
    public val default: OptionType
}

/**
 * A simple [Boolean] feature flag.
 */
public abstract class BooleanFeatureFlag(
    public override val key: String,
    public override val default: Boolean,
) : FeatureFlag<Boolean>

/**
 * A simple [String] feature flag.
 */
public abstract class StringFeatureFlag(
    public override val key: String,
    public override val default: String,
) : FeatureFlag<String>

/**
 * A simple [Double] feature flag.
 */
public abstract class DoubleFeatureFlag(
    public override val key: String,
    public override val default: Double,
) : FeatureFlag<Double>

/**
 * A simple [Long] feature flag.
 */
public abstract class LongFeatureFlag(
    public override val key: String,
    public override val default: Long,
) : FeatureFlag<Long>

/**
 * A simple [Long] feature flag.
 */
public abstract class ByteArrayFeatureFlag(
    public override val key: String,
    public override val default: ByteArray,
) : FeatureFlag<ByteArray>
