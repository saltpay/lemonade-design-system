import SwiftUI

// MARK: - Animatable Blur Modifier

/// An animatable modifier that applies a blur effect to a view.
/// Implements `Animatable` for smooth interpolation during animations.
///
/// ## Usage
/// ```swift
/// content
///     .modifier(AnimatableBlurModifier(radius: isBlurred ? 10 : 0))
///     .animation(.easeInOut, value: isBlurred)
/// ```
public struct AnimatableBlurModifier: ViewModifier, Animatable {
    /// The blur radius to apply.
    public var radius: CGFloat

    public var animatableData: CGFloat {
        get { radius }
        set { radius = newValue }
    }

    public init(radius: CGFloat = 0) {
        self.radius = radius
    }

    public func body(content: Content) -> some View {
        content.blur(radius: radius)
    }
}

// MARK: - View Extension for Blur

public extension View {
    /// Applies an animatable blur effect to the view.
    ///
    /// Unlike the standard `.blur()` modifier, this version properly
    /// interpolates the blur radius during animations.
    ///
    /// - Parameter radius: The blur radius to apply
    /// - Returns: A view with the animatable blur applied
    func animatableBlur(radius: CGFloat) -> some View {
        modifier(AnimatableBlurModifier(radius: radius))
    }
}

// MARK: - Custom Transitions

public extension AnyTransition {
    /// A transition that blurs the view in/out.
    ///
    /// - Parameter radius: The maximum blur radius during the transition. Defaults to 10.
    /// - Returns: A blur transition
    ///
    /// ## Usage
    /// ```swift
    /// if showContent {
    ///     ContentView()
    ///         .transition(.blur())
    /// }
    /// .animation(.easeInOut, value: showContent)
    /// ```
    static func blur(radius: CGFloat = 10) -> AnyTransition {
        .modifier(
            active: AnimatableBlurModifier(radius: radius),
            identity: AnimatableBlurModifier(radius: 0)
        )
    }

    /// A transition that combines blur with opacity fade.
    ///
    /// - Parameter radius: The maximum blur radius during the transition. Defaults to 10.
    /// - Returns: A combined blur and opacity transition
    static func blurWithOpacity(radius: CGFloat = 10) -> AnyTransition {
        blur(radius: radius).combined(with: .opacity)
    }

    /// A transition that combines scale, blur, and opacity for a "pop" effect.
    ///
    /// Useful for toast notifications and overlays that need to
    /// fade out when replaced.
    ///
    /// - Parameters:
    ///   - scale: The scale factor at the start/end of transition. Defaults to 0.88.
    ///   - blurRadius: The blur radius during transition. Defaults to 10.
    ///   - anchor: The anchor point for scaling. Defaults to `.center`.
    /// - Returns: A combined scale, blur, and opacity transition
    static func scaleBlurOpacity(
        scale: CGFloat = 0.88,
        blurRadius: CGFloat = 10,
        anchor: UnitPoint = .center
    ) -> AnyTransition {
        AnyTransition.scale(scale: scale, anchor: anchor)
            .combined(with: .blur(radius: blurRadius))
            .combined(with: .opacity)
    }
}

// MARK: - iOS 17+ Transition Protocol

/// A modern transition using the iOS 17+ Transition protocol.
/// Provides blur and opacity effects based on transition phase.
@available(iOS 17.0, macOS 14.0, tvOS 17.0, watchOS 10.0, *)
public struct BlurTransition: Transition {
    /// The blur radius when not in identity phase.
    public var radius: CGFloat

    /// Whether to also fade opacity.
    public var includeOpacity: Bool

    public init(radius: CGFloat = 10, includeOpacity: Bool = true) {
        self.radius = radius
        self.includeOpacity = includeOpacity
    }

    public func body(content: Content, phase: TransitionPhase) -> some View {
        content
            .blur(radius: phase.isIdentity ? 0 : radius)
            .opacity(phase.isIdentity || !includeOpacity ? 1 : 0)
    }
}

@available(iOS 17.0, macOS 14.0, tvOS 17.0, watchOS 10.0, *)
public extension Transition where Self == BlurTransition {
    /// A blur transition that fades the view with a blur effect.
    ///
    /// - Parameters:
    ///   - radius: The blur radius when transitioning. Defaults to 10.
    ///   - includeOpacity: Whether to also fade opacity. Defaults to true.
    /// - Returns: A blur transition
    static func blur(radius: CGFloat = 10, includeOpacity: Bool = true) -> BlurTransition {
        BlurTransition(radius: radius, includeOpacity: includeOpacity)
    }
}
