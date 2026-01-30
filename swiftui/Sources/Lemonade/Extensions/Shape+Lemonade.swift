import SwiftUI

// MARK: - Shape Namespace Extensions

/// Enables shorthand access for shape tokens
/// Usage:
/// ```swift
/// .clipShape(.shape.radius300)
/// .background(.bg.bgSubtle, in: .shape.radius200)
/// ```

// MARK: - Shapes Shorthand

public struct LemonadeShapesShorthand {
    public var radius0: RoundedRectangle { LemonadeRadius.radius0.shape }
    public var radius50: RoundedRectangle { LemonadeRadius.radius50.shape }
    public var radius100: RoundedRectangle { LemonadeRadius.radius100.shape }
    public var radius150: RoundedRectangle { LemonadeRadius.radius150.shape }
    public var radius200: RoundedRectangle { LemonadeRadius.radius200.shape }
    public var radius300: RoundedRectangle { LemonadeRadius.radius300.shape }
    public var radius400: RoundedRectangle { LemonadeRadius.radius400.shape }
    public var radius500: RoundedRectangle { LemonadeRadius.radius500.shape }
    public var radius600: RoundedRectangle { LemonadeRadius.radius600.shape }
    public var radius800: RoundedRectangle { LemonadeRadius.radius800.shape }
    public var radiusFull: RoundedRectangle { LemonadeRadius.radiusFull.shape }
}

// MARK: - Shape Extensions

public extension Shape where Self == RoundedRectangle {
    /// Shape tokens for RoundedRectangle
    /// Usage: `.clipShape(.shape.radius300)`
    static var shape: LemonadeShapesShorthand { LemonadeShapesShorthand() }
}
