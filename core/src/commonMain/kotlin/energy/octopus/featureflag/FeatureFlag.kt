package energy.octopus.featureflag

public interface FeatureFlag<OptionType : FeatureFlagOption> {

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
public abstract class BooleanFeatureFlag private constructor(
    public override val key: String,
    public override val default: BooleanOption,
) : FeatureFlag<BooleanOption> {

    public constructor(key: String, default: Boolean) : this(
        key = key,
        default = BooleanOption(default),
    )

    /**
     * Adapt a [raw] Boolean into a [BooleanOption].
     */
    public fun optionFrom(raw: Boolean): BooleanOption = BooleanOption(raw)
}

/**
 * A simple [String] feature flag.
 */
public abstract class StringFeatureFlag private constructor(
    public override val key: String,
    public override val default: StringOption,
) : FeatureFlag<StringOption> {

    public constructor(key: String, default: String) : this(
        key = key,
        default = StringOption(default),
    )

    /**
     * Adapt a [raw] String into a [StringOption].
     */
    public fun optionFrom(raw: String): StringOption = StringOption(raw)
}

/**
 * A simple [Double] feature flag.
 */
public abstract class DoubleFeatureFlag private constructor(
    public override val key: String,
    public override val default: DoubleOption,
) : FeatureFlag<DoubleOption> {

    public constructor(key: String, default: Double) : this(
        key = key,
        default = DoubleOption(default),
    )

    /**
     * Adapt a [raw] Double into a [DoubleOption].
     */
    public fun optionFrom(raw: Double): DoubleOption = DoubleOption(raw)
}

/**
 * A simple [Long] feature flag.
 */
public abstract class LongFeatureFlag private constructor(
    public override val key: String,
    public override val default: LongOption,
) : FeatureFlag<LongOption> {

    public constructor(key: String, default: Long) : this(
        key = key,
        default = LongOption(default),
    )

    /**
     * Adapt a [raw] Double into a [LongOption].
     */
    public fun optionFrom(raw: Long): LongOption = LongOption(raw)
}

/**
 * A simple [Long] feature flag.
 */
public abstract class ByteArrayFeatureFlag private constructor(
    public override val key: String,
    public override val default: ByteArrayOption,
) : FeatureFlag<ByteArrayOption> {

    public constructor(key: String, default: ByteArray) : this(
        key = key,
        default = ByteArrayOption(default),
    )

    /**
     * Adapt a [raw] Double into a [LongOption].
     */
    public fun optionFrom(raw: ByteArray): ByteArrayOption = ByteArrayOption(raw)
}
