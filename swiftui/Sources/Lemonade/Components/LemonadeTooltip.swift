import SwiftUI

// MARK: - Tooltip Indicator Placement

/// Where the tooltip draws its indicator — the small arrow that points back at the element the
/// tooltip describes.
///
/// Placement is physical, not directional: `.topLeft` anchors the indicator to the left edge in both
/// LTR and RTL layouts, so a caller that positions the tooltip by absolute coordinates does not have
/// to compensate for the layout direction.
public enum LemonadeTooltipIndicatorPlacement: Hashable, CaseIterable, Sendable {
    /// No indicator. The tooltip is a plain floating container.
    case none

    case topLeft
    case topCenter
    case topRight
    case bottomLeft
    case bottomCenter
    case bottomRight

    /// Whether the indicator protrudes above the tooltip body.
    var pointsUp: Bool {
        switch self {
        case .topLeft, .topCenter, .topRight: return true
        case .none, .bottomLeft, .bottomCenter, .bottomRight: return false
        }
    }

    /// Whether the indicator protrudes below the tooltip body.
    var pointsDown: Bool {
        switch self {
        case .bottomLeft, .bottomCenter, .bottomRight: return true
        case .none, .topLeft, .topCenter, .topRight: return false
        }
    }

    /// Horizontal centre of the indicator within a body of the given width.
    func indicatorCenterX(in width: CGFloat) -> CGFloat {
        switch self {
        case .topLeft, .bottomLeft:
            return LemonadeTooltipMetrics.indicatorEdgeInset + LemonadeTooltipMetrics.indicatorBaseWidth / 2
        case .topCenter, .bottomCenter, .none:
            return width / 2
        case .topRight, .bottomRight:
            return width - LemonadeTooltipMetrics.indicatorEdgeInset - LemonadeTooltipMetrics.indicatorBaseWidth / 2
        }
    }
}

// MARK: - Tooltip Footer Action Variant

/// Emphasis of a tooltip footer action.
public enum LemonadeTooltipFooterActionVariant: Hashable, Sendable {
    /// Filled action. Use for the single most important action in the footer.
    case primary

    /// Outlined action. Use for secondary or dismissive actions.
    case secondary

    var backgroundColor: Color {
        switch self {
        case .primary: return LemonadeTheme.colors.background.bgAlwaysLight
        case .secondary: return Color.clear
        }
    }

    var contentColor: Color {
        switch self {
        case .primary: return LemonadeTheme.colors.content.contentAlwaysDark
        case .secondary: return LemonadeTheme.colors.content.contentAlwaysLight
        }
    }

    var textStyle: LemonadeTextStyle {
        switch self {
        case .primary: return LemonadeTypography.shared.bodySmallSemiBold
        case .secondary: return LemonadeTypography.shared.bodySmallMedium
        }
    }

    var borderWidth: CGFloat {
        switch self {
        case .primary: return 0
        case .secondary: return LemonadeTheme.borderWidth.base.border25
        }
    }
}

// MARK: - Metrics

private enum LemonadeTooltipMetrics {
    /// Default tooltip width.
    static let width: CGFloat = 280

    /// Height of the visible, rounded-off indicator — how far it protrudes past the tooltip body.
    static let indicatorHeight: CGFloat = 8

    /// Width of the indicator where it meets the tooltip body.
    static let indicatorBaseWidth: CGFloat = 15

    /// Height the indicator would reach if its tip were a sharp point instead of a rounded one.
    static let indicatorApexHeight: CGFloat = 10

    /// Distance from the tooltip edge to the near side of the indicator base, for left/right placements.
    static let indicatorEdgeInset: CGFloat = 40

    /// Fraction of the way from the indicator base to its notional apex at which the rounded tip starts.
    /// Together with `indicatorTipCurvature` this yields the 3pt tip radius from the design.
    static let indicatorTipStart: CGFloat = 0.68

    /// How far each tip control point is pulled towards the notional apex.
    static let indicatorTipCurvature: CGFloat = 0.5

    /// Cover slot aspect ratio — 272x158, the cover size at the default 280pt tooltip width.
    static let coverAspectRatio: CGFloat = 272.0 / 158.0
}

// MARK: - Tooltip Footer Scope

/// Vends the parts that belong in a tooltip footer.
///
/// The footer is a slot, but only a fixed set of parts belongs in it — they are produced by this
/// scope rather than being standalone components, so they can only be used where they make sense.
/// The scope is handed to the footer builder; you cannot create one yourself.
public struct LemonadeTooltipFooterScope {

    fileprivate init() {}

    /// Progress indicator for a multi-step tooltip tour, rendered as `1 of 3`.
    ///
    /// Follow it with a `Spacer()` to push the actions to the trailing edge.
    ///
    /// - Parameters:
    ///   - currentStep: The step being shown, 1-based.
    ///   - totalSteps: The total number of steps in the tour.
    ///   - separator: Word placed between the two numbers. Defaults to `"of"` — pass a translated
    ///     string to localise it.
    /// - Returns: The step counter view.
    public func stepCounter(
        currentStep: Int,
        totalSteps: Int,
        separator: String = "of"
    ) -> some View {
        LemonadeUi.Text(
            "\(currentStep) \(separator) \(totalSteps)",
            textStyle: LemonadeTypography.shared.bodyXSmallMedium,
            color: LemonadeTheme.colors.content.contentAlwaysLight
                .opacity(LemonadeTheme.opacity.base.opacity80),
            maxLines: 1
        )
        .fixedSize(horizontal: true, vertical: false)
    }

    /// A tappable action in the tooltip footer.
    ///
    /// - Parameters:
    ///   - label: The text shown on the action.
    ///   - variant: Emphasis of the action. Defaults to `.primary`.
    ///   - onClick: Invoked when the action is tapped.
    /// - Returns: The action view.
    public func action(
        _ label: String,
        variant: LemonadeTooltipFooterActionVariant = .primary,
        onClick: @escaping () -> Void
    ) -> some View {
        LemonadeTooltipFooterActionView(
            label: label,
            variant: variant,
            onClick: onClick
        )
    }
}

// MARK: - Tooltip Component

public extension LemonadeUi {

    /// A floating container that explains or draws attention to another element on screen.
    ///
    /// The tooltip renders its own surface, indicator and content — it does not position itself.
    /// Place it in an overlay or popover of your choosing and pick the `indicatorPlacement` that
    /// points back at the element being described.
    ///
    /// The tooltip is 280pt wide by default. The indicator is drawn outside the body, so the view is
    /// 8pt taller than the body itself whenever `indicatorPlacement` is not `.none`.
    ///
    /// ## Usage
    /// ```swift
    /// // Text only
    /// LemonadeUi.Tooltip(
    ///     content: "Tap here to see everything you sold today.",
    ///     title: "Daily takings",
    ///     indicatorPlacement: .topLeft
    /// )
    ///
    /// // Guided tour step, with a cover and a footer
    /// LemonadeUi.Tooltip(
    ///     content: "Tap here to see everything you sold today.",
    ///     title: "Daily takings",
    ///     indicatorPlacement: .topLeft,
    ///     onClose: { isPresented = false },
    ///     cover: { Image("takings-illustration").resizable() },
    ///     footer: { footer in
    ///         footer.stepCounter(currentStep: step, totalSteps: 3)
    ///         Spacer()
    ///         footer.action("Skip", variant: .secondary) { isPresented = false }
    ///         footer.action("Next") { step += 1 }
    ///     }
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - content: The body text. Required — a tooltip always says something.
    ///   - title: Optional bold heading shown above `content`.
    ///   - indicatorPlacement: Where the indicator points from. Defaults to `.none`, which draws no
    ///     indicator.
    ///   - onClose: Invoked when the close button is tapped. The close button is only shown when
    ///     this is non-nil.
    ///   - closeContentDescription: Accessibility label for the close button.
    ///   - cover: Optional slot rendered above the text, in a 272:158 box clipped to the tooltip's
    ///     top corners. Use it for an illustration or screenshot.
    ///   - footer: Optional slot rendered below the text. See `LemonadeTooltipFooterScope` for the
    ///     parts that belong in it.
    /// - Returns: A styled Tooltip view
    @ViewBuilder
    static func Tooltip<Cover: View, Footer: View>(
        content: String,
        title: String? = nil,
        indicatorPlacement: LemonadeTooltipIndicatorPlacement = .none,
        onClose: (() -> Void)? = nil,
        closeContentDescription: String? = nil,
        @ViewBuilder cover: @escaping () -> Cover = { EmptyView() },
        @ViewBuilder footer: @escaping (LemonadeTooltipFooterScope) -> Footer = { _ in EmptyView() }
    ) -> some View {
        // The slots are type-erased here so the view itself can treat "absent" as nil rather than
        // inferring it from a generic parameter. The container builds tooltips from stored,
        // already-erased slots, where that inference is not available.
        LemonadeTooltipView(
            content: content,
            title: title,
            indicatorPlacement: indicatorPlacement,
            onClose: onClose,
            closeContentDescription: closeContentDescription,
            cover: Cover.self == EmptyView.self ? nil : AnyView(cover()),
            footer: Footer.self == EmptyView.self ? nil : { scope in AnyView(footer(scope)) }
        )
    }
}

// MARK: - Internal Tooltip View

struct LemonadeTooltipView: View {
    let content: String
    let title: String?
    let indicatorPlacement: LemonadeTooltipIndicatorPlacement
    let onClose: (() -> Void)?
    let closeContentDescription: String?
    let cover: AnyView?
    let footer: ((LemonadeTooltipFooterScope) -> AnyView)?

    private var hasCover: Bool { cover != nil }
    private var hasFooter: Bool { footer != nil }

    // The indicator is drawn outside the body, so the body has to be inset by its height on
    // whichever edge the indicator sits on.
    private var indicatorTopInset: CGFloat {
        indicatorPlacement.pointsUp ? LemonadeTooltipMetrics.indicatorHeight : 0
    }

    private var indicatorBottomInset: CGFloat {
        indicatorPlacement.pointsDown ? LemonadeTooltipMetrics.indicatorHeight : 0
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            if hasCover {
                coverView
            }

            bodyView
        }
        .padding(LemonadeTheme.spaces.spacing100)
        .padding(.top, indicatorTopInset)
        .padding(.bottom, indicatorBottomInset)
        .frame(width: LemonadeTooltipMetrics.width)
        .background(surface)
        .background(shadowLayer)
        .overlay(alignment: .topTrailing) {
            if let onClose {
                closeButton(onClose: onClose)
                    .padding(.top, indicatorTopInset + LemonadeTheme.spaces.spacing100)
                    .padding(.trailing, LemonadeTheme.spaces.spacing100)
            }
        }
    }

    // MARK: Surface

    /// The tooltip surface: a blurred backdrop with the fill token layered over it.
    ///
    /// The fill is only ~74% opaque (an 80% layer opacity over `bgAlwaysDark`), so whatever the
    /// tooltip covers shows through. Blurring the backdrop keeps that legible instead of letting
    /// busy content read through the text.
    private var surface: some View {
        let shape = LemonadeTooltipShape(
            indicatorPlacement: indicatorPlacement,
            cornerRadius: LemonadeTheme.radius.radius600
        )

        return ZStack {
            // `.ultraThinMaterial` is the backdrop blur; the token fill on top restores the exact
            // colour the design calls for, so the material's own tint barely reads through.
            shape.fill(.ultraThinMaterial)

            shape.fill(
                LemonadeTheme.colors.background.bgAlwaysDark
                    .opacity(LemonadeTheme.opacity.base.opacity80)
            )
        }
    }

    /// The drop shadow, cast from an opaque silhouette of the surface rather than from the surface
    /// itself.
    ///
    /// `lemonadeShadow` builds its shadow by masking a solid colour with the view it is applied to,
    /// so applying it to the tooltip scaled the shadow by the surface's ~74% alpha and roughly
    /// halved it — measured at 4.7% against Compose's 10% at the same edge. Casting it from an
    /// opaque copy of the shape keeps the token at full strength, and punching that shape back out
    /// stops the shadow from tinting the translucent fill sitting in front of it.
    private var shadowLayer: some View {
        let shape = LemonadeTooltipShape(
            indicatorPlacement: indicatorPlacement,
            cornerRadius: LemonadeTheme.radius.radius600
        )

        return shape
            .fill(Color.black)
            .lemonadeShadow(.xlarge)
            .overlay {
                shape
                    .fill(Color.black)
                    .blendMode(.destinationOut)
            }
            .compositingGroup()
    }

    // MARK: Cover

    private var coverView: some View {
        // The cover sits inside the tooltip's outer padding, so its top corners are the tooltip's
        // own radius less that padding; the bottom corners are the smaller radius the design
        // specifies.
        Color.clear
            .aspectRatio(LemonadeTooltipMetrics.coverAspectRatio, contentMode: .fit)
            .overlay { cover }
            .background(LemonadeTheme.colors.background.bgElevatedHigh)
            .clipShape(
                LemonadeTooltipCoverShape(
                    topRadius: LemonadeTheme.radius.radius600 - LemonadeTheme.spaces.spacing100,
                    bottomRadius: LemonadeTheme.radius.radius250
                )
            )
    }

    // MARK: Body

    private var bodyView: some View {
        VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing300) {
            VStack(alignment: .leading, spacing: 0) {
                if let title {
                    LemonadeUi.Text(
                        title,
                        textStyle: LemonadeTypography.shared.bodySmallSemiBold,
                        color: LemonadeTheme.colors.content.contentAlwaysLight
                    )
                    .frame(maxWidth: .infinity, alignment: .leading)
                }

                LemonadeUi.Text(
                    content,
                    textStyle: LemonadeTypography.shared.bodySmallRegular,
                    color: LemonadeTheme.colors.content.contentAlwaysLight
                        .opacity(LemonadeTheme.opacity.base.opacity90)
                )
                .frame(maxWidth: .infinity, alignment: .leading)
            }

            if let footer {
                HStack(spacing: LemonadeTheme.spaces.spacing100) {
                    footer(LemonadeTooltipFooterScope())
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
        }
        .padding(LemonadeTheme.spaces.spacing300)
    }

    // MARK: Close button

    private func closeButton(onClose: @escaping () -> Void) -> some View {
        SwiftUI.Button(action: onClose) {
            LemonadeUi.Icon(
                icon: .times,
                contentDescription: closeContentDescription,
                size: .small,
                tint: LemonadeTheme.colors.content.contentPrimaryInverse
            )
            .frame(
                width: LemonadeTheme.sizes.size800,
                height: LemonadeTheme.sizes.size800
            )
            .contentShape(Circle())
        }
        .buttonStyle(.plain)
        .opacity(LemonadeTheme.opacity.base.opacity60)
    }
}

// MARK: - Footer Action View

private struct LemonadeTooltipFooterActionView: View {
    let label: String
    let variant: LemonadeTooltipFooterActionVariant
    let onClick: () -> Void

    var body: some View {
        SwiftUI.Button(action: onClick) {
            LemonadeUi.Text(
                label,
                textStyle: variant.textStyle,
                color: variant.contentColor,
                maxLines: 1
            )
            .padding(.horizontal, LemonadeTheme.spaces.spacing300)
            .padding(.vertical, LemonadeTheme.spaces.spacing100)
            .frame(minHeight: LemonadeTheme.sizes.size800)
            .background(variant.backgroundColor)
            .clipShape(Capsule())
            .overlay {
                Capsule()
                    .strokeBorder(
                        LemonadeTheme.colors.border.borderNeutralLowInverse,
                        lineWidth: variant.borderWidth
                    )
            }
        }
        .buttonStyle(.plain)
        .fixedSize(horizontal: true, vertical: false)
    }
}

// MARK: - Tooltip Shape

/// The tooltip surface: a rounded rectangle with the indicator triangle added to one edge.
///
/// The two are separate sub-paths of a single `Path` rather than two drawn shapes. Non-zero winding
/// fills overlapping sub-paths as their union in one operation, so the join is seamless — drawing
/// them as two views at the surface's ~74% alpha would composite the overlap twice and show a
/// darker wedge across the join. Being one shape also means one shadow outline and one clip for the
/// backdrop blur.
///
/// The indicator is drawn outside the body, so the shape expects a rect that already includes
/// `indicatorHeight` on the indicator's edge.
private struct LemonadeTooltipShape: Shape {
    let indicatorPlacement: LemonadeTooltipIndicatorPlacement
    let cornerRadius: CGFloat

    func path(in rect: CGRect) -> Path {
        let indicatorHeight = LemonadeTooltipMetrics.indicatorHeight
        let bodyTop = indicatorPlacement.pointsUp ? indicatorHeight : 0
        let bodyBottom = indicatorPlacement.pointsDown ? rect.height - indicatorHeight : rect.height
        let radius = min(cornerRadius, min(rect.width, bodyBottom - bodyTop) / 2)

        var path = Path()
        path.addRoundedRect(
            in: CGRect(x: 0, y: bodyTop, width: rect.width, height: bodyBottom - bodyTop),
            cornerSize: CGSize(width: radius, height: radius)
        )

        // Appended as a second sub-path and unioned by the non-zero fill rule. This works because
        // CoreGraphics winds `addRoundedRect` the same way as the triangle below; reverse either and
        // they cancel where they overlap, leaving a hairline hole across the join. Compose needs an
        // explicit boolean union for exactly that reason — Skia winds its round rect the other way.
        if indicatorPlacement != .none {
            path.addPath(
                indicatorPath(
                    in: rect,
                    baseY: indicatorPlacement.pointsUp ? bodyTop : bodyBottom
                )
            )
        }

        return path
    }

    /// The indicator triangle. Its straight edges stop short of the notional apex and a cubic
    /// bridges the gap, which is what rounds the tip. The base is deliberately sunk one point into
    /// the body so the two sub-paths overlap rather than merely touching, which would leave a
    /// hairline.
    private func indicatorPath(in rect: CGRect, baseY: CGFloat) -> Path {
        let halfBase = LemonadeTooltipMetrics.indicatorBaseWidth / 2
        let apexHeight = LemonadeTooltipMetrics.indicatorApexHeight
        let tipStart = LemonadeTooltipMetrics.indicatorTipStart
        let curvature = LemonadeTooltipMetrics.indicatorTipCurvature

        let pointsUp = indicatorPlacement.pointsUp
        let centerX = indicatorPlacement.indicatorCenterX(in: rect.width)
        let overlap: CGFloat = pointsUp ? 1 : -1
        let apexY = pointsUp ? baseY - apexHeight : baseY + apexHeight

        let tipEntryX = (centerX - halfBase) + halfBase * tipStart
        let tipExitX = (centerX + halfBase) - halfBase * tipStart
        let tipY = baseY + (apexY - baseY) * tipStart
        let controlY = tipY + (apexY - tipY) * curvature

        var path = Path()
        path.move(to: CGPoint(x: centerX - halfBase, y: baseY + overlap))
        path.addLine(to: CGPoint(x: tipEntryX, y: tipY))
        path.addCurve(
            to: CGPoint(x: tipExitX, y: tipY),
            control1: CGPoint(x: tipEntryX + (centerX - tipEntryX) * curvature, y: controlY),
            control2: CGPoint(x: tipExitX + (centerX - tipExitX) * curvature, y: controlY)
        )
        path.addLine(to: CGPoint(x: centerX + halfBase, y: baseY + overlap))
        path.closeSubpath()

        return path
    }
}

/// Rectangle with independent top and bottom corner radii, for the cover slot.
///
/// `UnevenRoundedRectangle` would do this, but it is iOS 16+ and the package targets iOS 15.
private struct LemonadeTooltipCoverShape: Shape {
    let topRadius: CGFloat
    let bottomRadius: CGFloat

    func path(in rect: CGRect) -> Path {
        let maxRadius = min(rect.width, rect.height) / 2
        let top = min(topRadius, maxRadius)
        let bottom = min(bottomRadius, maxRadius)

        var path = Path()
        path.move(to: CGPoint(x: top, y: 0))
        path.addLine(to: CGPoint(x: rect.width - top, y: 0))
        path.addArc(
            center: CGPoint(x: rect.width - top, y: top),
            radius: top,
            startAngle: .degrees(-90),
            endAngle: .degrees(0),
            clockwise: false
        )
        path.addLine(to: CGPoint(x: rect.width, y: rect.height - bottom))
        path.addArc(
            center: CGPoint(x: rect.width - bottom, y: rect.height - bottom),
            radius: bottom,
            startAngle: .degrees(0),
            endAngle: .degrees(90),
            clockwise: false
        )
        path.addLine(to: CGPoint(x: bottom, y: rect.height))
        path.addArc(
            center: CGPoint(x: bottom, y: rect.height - bottom),
            radius: bottom,
            startAngle: .degrees(90),
            endAngle: .degrees(180),
            clockwise: false
        )
        path.addLine(to: CGPoint(x: 0, y: top))
        path.addArc(
            center: CGPoint(x: top, y: top),
            radius: top,
            startAngle: .degrees(180),
            endAngle: .degrees(270),
            clockwise: false
        )
        path.closeSubpath()

        return path
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeTooltip_Previews: PreviewProvider {
    static var previews: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 24) {
                sectionTitle("Indicator Placements")
                ForEach(LemonadeTooltipIndicatorPlacement.allCases, id: \.self) { placement in
                    LemonadeUi.Tooltip(
                        content: "Tap here to see everything you sold today.",
                        title: placementName(placement),
                        indicatorPlacement: placement
                    )
                }

                sectionTitle("Content Only")
                LemonadeUi.Tooltip(
                    content: "A tooltip with no title, no cover and no footer.",
                    indicatorPlacement: .topCenter
                )

                sectionTitle("With Close Button")
                LemonadeUi.Tooltip(
                    content: "Close buttons appear only when you pass onClose.",
                    title: "Dismissible",
                    indicatorPlacement: .topRight,
                    onClose: {},
                    closeContentDescription: "Close"
                )

                sectionTitle("With Cover")
                LemonadeUi.Tooltip(
                    content: "The cover slot takes any view — an illustration, a screenshot, a video.",
                    title: "Cover slot",
                    indicatorPlacement: .bottomCenter,
                    cover: { LemonadeTheme.colors.background.bgBrandSubtle }
                )

                sectionTitle("With Footer")
                LemonadeUi.Tooltip(
                    content: "The footer is a scoped slot — only the step counter and actions belong in it.",
                    title: "Step 1",
                    indicatorPlacement: .topLeft,
                    onClose: {},
                    closeContentDescription: "Close",
                    footer: { footer in
                        footer.stepCounter(currentStep: 1, totalSteps: 3)
                        Spacer()
                        footer.action("Skip", variant: .secondary) {}
                        footer.action("Next") {}
                    }
                )
            }
            .padding()
        }
        .background(LemonadeTheme.colors.background.bgSubtle)
        .previewLayout(.sizeThatFits)
    }

    private static func placementName(_ placement: LemonadeTooltipIndicatorPlacement) -> String {
        switch placement {
        case .none: return "None"
        case .topLeft: return "Top Left"
        case .topCenter: return "Top Center"
        case .topRight: return "Top Right"
        case .bottomLeft: return "Bottom Left"
        case .bottomCenter: return "Bottom Center"
        case .bottomRight: return "Bottom Right"
        }
    }

    @ViewBuilder
    private static func sectionTitle(_ text: String) -> some View {
        LemonadeUi.Text(
            text,
            textStyle: LemonadeTypography.shared.headingXSmall,
            color: LemonadeTheme.colors.content.contentSecondary
        )
        .padding(.top, 8)
    }
}
#endif
