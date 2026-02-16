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
 * - [XSmall]: Extra small size (48dp for circles)
 * - [Small]: Small size (64dp for circles)
 * - [Medium]: Medium size (80dp for circles) - default
 * - [Large]: Large size (96dp for circles)
 * - [XLarge]: Extra large size (128dp for circles)
 * - [XXLarge]: Double extra large size (160dp for circles)
 * - [XXXLarge]: Triple extra large size (192dp for circles)
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

/**
 * Enumeration of skeleton shape variants.
 *
 * Defines the shape and styling of the skeleton placeholder. Each variant is designed
 * to match specific content types and provides appropriate dimensions and corner radius.
 *
 * ## Variants
 * - [Line]: Horizontal line skeleton for text content. Uses height-based sizing from [LemonadeSkeletonSize]
 *   with unspecified width. Applied vertical spacing and small corner radius.
 *
 * - [Circle]: Circular skeleton for avatar and image content. Uses equal width/height
 *   from [LemonadeSkeletonSize] to create a perfect circle. Full rounded corner radius.
 *
 * - [Block]: Large block skeleton for card and image content. Uses fixed height (1600dp)
 *   with unspecified width. Large rounded corner radius suitable for prominent content.
 *
 * ## Usage
 * ```kotlin
 * // Text placeholder
 * LemonadeUi.LineSkeleton(modifier = Modifier.fillMaxWidth())
 *
 * // Avatar placeholder
 * LemonadeUi.CircleSkeleton()
 *
 * // Card placeholder
 * LemonadeUi.BlockSkeleton(modifier = Modifier.fillMaxWidth())
 * ```
 */
public enum class LemonadeSkeletonVariant {
    Line,
    Circle,
    Block
}
