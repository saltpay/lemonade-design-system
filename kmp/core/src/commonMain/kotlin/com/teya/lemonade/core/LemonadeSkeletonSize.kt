package com.teya.lemonade.core

/**
 * Enumeration of available skeleton size options.
 *
 * Defines the size scale for skeleton placeholders. Each size corresponds to a specific
 * dimension in the design system's sizing scale. The actual pixel values depend on the
 * skeleton variant and are resolved from the design system's composition locals.
 *
 * ## Size Scale
 * Ordered from smallest to largest:
 * - [XSmall]: Extra small size for very compact skeletons
 * - [Small]: Small size for compact skeletons
 * - [Medium]: Medium size for standard skeletons (default)
 * - [Large]: Large size for slightly more prominent skeletons
 * - [XLarge]: Extra large size for prominent skeletons
 * - [XXLarge]: Double extra large size for very prominent skeletons
 * - [XXXLarge]: Triple extra large size for the largest skeletons
 *
 * Note: Concrete dp values for each size are derived from the design system sizing
 * tokens (for example, `LemonadeSizes`) and may change over time. Consumers should rely
 * on these semantic sizes rather than specific dp values.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.LineSkeleton(size = LemonadeSkeletonSize.Large)
 * LemonadeUi.CircleSkeleton(size = LemonadeSkeletonSize.Small)
 * ```
 */
public enum class LemonadeSkeletonSize {
    XSmall,
    Small,
    Medium,
    Large,
    XLarge,
    XXLarge,
    XXXLarge,
}
