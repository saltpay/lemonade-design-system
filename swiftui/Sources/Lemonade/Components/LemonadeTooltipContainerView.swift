import SwiftUI

// MARK: - Layout Metrics

private enum LemonadeTooltipLayout {
    /// Gap between the indicator tip and the anchor it points at.
    static let anchorGap: CGFloat = 4

    /// Smallest distance the tooltip keeps from the edges of the container.
    static let containerMargin: CGFloat = 8

    /// Space left around the anchor when the spotlight scrim punches through.
    static let spotlightPadding: CGFloat = 4

    /// Corner radius of the spotlight cut-out.
    static let spotlightRadius: CGFloat = 12

}

// MARK: - Anchor Registration

/// Reports this view's global frame to the manager under `key`.
///
/// Frames are reported in global coordinates and translated by the container, rather than published
/// through a `PreferenceKey`: preferences do not propagate out of a `NavigationStack` destination,
/// so an anchor on a pushed screen would never reach the container. The environment does propagate
/// down, so writing to the manager directly works from anywhere inside the container.
private struct LemonadeTooltipAnchorModifier: ViewModifier {
    let key: String

    @EnvironmentObject private var manager: LemonadeTooltipManager

    func body(content: Content) -> some View {
        content.background(
            GeometryReader { proxy in
                let frame = proxy.frame(in: .global)

                Color.clear
                    .onAppear { manager.updateAnchor(key, frame: frame) }
                    .onChange(of: frame) { newFrame in
                        manager.updateAnchor(key, frame: newFrame)
                    }
                    .onDisappear { manager.removeAnchor(key) }
            }
        )
    }
}

public extension View {
    /// Registers this view as a tooltip anchor under `key`, so ``LemonadeTooltipManager/show(anchor:content:title:indicatorPlacement:scrim:dismissOnOutsideTap:showCloseButton:closeContentDescription:)``
    /// and ``LemonadeTooltipManager/startTour(steps:labels:scrim:onFinish:onSkip:)`` can point at it.
    ///
    /// Keys are global to the enclosing `lemonadeTooltipContainer()`. A tooltip aimed at an anchor
    /// that is not on screen simply does not render — which is what lets a tour walk across several
    /// screens.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Button(label: "Takings") { }
    ///     .lemonadeTooltipAnchor("takings")
    /// ```
    func lemonadeTooltipAnchor(_ key: String) -> some View {
        modifier(LemonadeTooltipAnchorModifier(key: key))
    }

    /// Adds a tooltip container to this view hierarchy.
    ///
    /// Apply this modifier to your root view to enable anchored tooltips and guided tours. It
    /// provides a ``LemonadeTooltipManager`` as an environment object and renders the tooltip
    /// overlay — the scrim and the anchored tooltip itself — above your content.
    ///
    /// ## Usage
    /// ```swift
    /// @main
    /// struct MyApp: App {
    ///     var body: some Scene {
    ///         WindowGroup {
    ///             ContentView()
    ///                 .lemonadeTooltipContainer()
    ///         }
    ///     }
    /// }
    /// ```
    func lemonadeTooltipContainer() -> some View {
        LemonadeTooltipContainerView(content: self)
    }
}

// MARK: - Container

struct LemonadeTooltipContainerView<Content: View>: View {
    let content: Content

    @StateObject private var manager = LemonadeTooltipManager()
    @State private var tooltipSize: CGSize = .zero

    var body: some View {
        // The overlay hangs off `content` rather than wrapping it in a ZStack: a NavigationStack
        // nested inside a GeometryReader/ZStack takes over the full screen through UIKit and paints
        // over its SwiftUI siblings, so an overlay built that way never becomes visible. This is the
        // same structure the toast container uses.
        content
            .environmentObject(manager)
            .overlay {
                GeometryReader { proxy in
                    let containerFrame = proxy.frame(in: .global)

                    ZStack(alignment: .topLeading) {
                        if let presentation = manager.presentation,
                           let anchor = manager.anchorFrameInContainer(presentation.anchor) {
                            overlay(
                                presentation: presentation,
                                anchor: anchor,
                                containerSize: proxy.size
                            )
                        }
                    }
                    .onAppear { manager.containerFrame = containerFrame }
                    .onChange(of: containerFrame) { newFrame in
                        manager.containerFrame = newFrame
                    }
                }
                // Without this the overlay is inset by the safe area while anchors are reported in
                // global coordinates, so everything would sit a status bar's height too low.
                .ignoresSafeArea()
            }
    }

    // MARK: Overlay

    @ViewBuilder
    private func overlay(
        presentation: LemonadeTooltipPresentation,
        anchor: CGRect,
        containerSize: CGSize
    ) -> some View {
        let pointsUp = LemonadeTooltipPositioning.pointsUp(anchor: anchor, containerSize: containerSize)
        let placement = presentation.indicatorPlacement
            ?? LemonadeTooltipPositioning.indicatorPlacement(
                anchor: anchor,
                containerSize: containerSize,
                tooltipWidth: tooltipSize.width > 0 ? tooltipSize.width : 280,
                pointsUp: pointsUp
            )
        let origin = LemonadeTooltipPositioning.origin(
            anchor: anchor,
            containerSize: containerSize,
            tooltipSize: tooltipSize,
            placement: placement,
            pointsUp: pointsUp
        )

        ZStack(alignment: .topLeading) {
            scrim(presentation: presentation, anchor: anchor)

            tooltip(presentation: presentation, placement: placement)
                .background(
                    GeometryReader { proxy in
                        Color.clear.preference(
                            key: LemonadeTooltipSizePreferenceKey.self,
                            value: proxy.size
                        )
                    }
                )
                .offset(x: origin.x, y: origin.y)
                // Hidden until measured, so it never flashes at the wrong position.
                .opacity(tooltipSize == .zero ? 0 : 1)
        }
        .onPreferenceChange(LemonadeTooltipSizePreferenceKey.self) { size in
            tooltipSize = size
        }
        // Deliberately not reset when a step disappears: the outgoing step's teardown runs after the
        // incoming one has measured, so clearing it there would race and leave the new tooltip stuck
        // at zero size, and therefore invisible. Carrying the previous size over is harmless — it is
        // only used until the new measurement lands a frame later.
        .id(presentation.id)
    }

    @ViewBuilder
    private func scrim(
        presentation: LemonadeTooltipPresentation,
        anchor: CGRect
    ) -> some View {
        let scrimColor = LemonadeTheme.colors.background.bgAlwaysDark
            .opacity(LemonadeTheme.opacity.base.opacity40)

        Group {
            switch presentation.scrim {
            case .none:
                Color.clear

            case .dim:
                scrimColor

            case .spotlight:
                GeometryReader { proxy in
                    Path { path in
                        path.addRect(CGRect(origin: .zero, size: proxy.size))
                        path.addRoundedRect(
                            in: anchor.insetBy(
                                dx: -LemonadeTooltipLayout.spotlightPadding,
                                dy: -LemonadeTooltipLayout.spotlightPadding
                            ),
                            cornerSize: CGSize(
                                width: LemonadeTooltipLayout.spotlightRadius,
                                height: LemonadeTooltipLayout.spotlightRadius
                            )
                        )
                    }
                    .fill(scrimColor, style: FillStyle(eoFill: true))
                }
            }
        }
        .contentShape(Rectangle())
        .onTapGesture {
            // The tap is always swallowed so the UI underneath cannot be acted on while a tooltip
            // is up; whether it also dismisses is the caller's choice.
            if presentation.dismissOnOutsideTap {
                manager.dismiss()
            }
        }
    }

    private func tooltip(
        presentation: LemonadeTooltipPresentation,
        placement: LemonadeTooltipIndicatorPlacement
    ) -> some View {
        let onClose: (() -> Void)? = presentation.showCloseButton ? { manager.dismiss() } : nil

        let footer: ((LemonadeTooltipFooterScope) -> AnyView)?
        if presentation.isTourStep, let labels = manager.tourLabels {
            footer = { scope in
                AnyView(
                    tourFooter(footer: scope, presentation: presentation, labels: labels)
                )
            }
        } else {
            footer = presentation.footer
        }

        return LemonadeTooltipView(
            content: presentation.content,
            title: presentation.title,
            indicatorPlacement: placement,
            onClose: onClose,
            closeContentDescription: presentation.closeContentDescription,
            cover: presentation.cover,
            footer: footer
        )
    }

    @ViewBuilder
    private func tourFooter(
        footer: LemonadeTooltipFooterScope,
        presentation: LemonadeTooltipPresentation,
        labels: LemonadeTooltipTourLabels
    ) -> some View {
        let isLast = presentation.stepIndex == presentation.stepCount - 1

        footer.stepCounter(
            currentStep: presentation.stepIndex + 1,
            totalSteps: presentation.stepCount,
            separator: labels.stepSeparator
        )

        Spacer()

        if let skip = labels.skip, !isLast {
            footer.action(skip, variant: .secondary) { manager.skip() }
        }

        footer.action(isLast ? labels.done : labels.next) { manager.next() }
    }
}

private struct LemonadeTooltipSizePreferenceKey: PreferenceKey {
    static var defaultValue: CGSize { .zero }

    static func reduce(value: inout CGSize, nextValue: () -> CGSize) {
        let next = nextValue()
        if next != .zero {
            value = next
        }
    }
}

// MARK: - Positioning

enum LemonadeTooltipPositioning {

    /// Whether the tooltip sits below the anchor, with its indicator pointing up at it.
    static func pointsUp(anchor: CGRect, containerSize: CGSize) -> Bool {
        anchor.midY < containerSize.height / 2
    }

    /// Horizontal distance from the tooltip's leading edge to the centre of its indicator. The
    /// indicator sits at one of three fixed positions, so this is the reach a placement can offer.
    static func indicatorCenterOffset(
        placement: LemonadeTooltipIndicatorPlacement,
        tooltipWidth: CGFloat
    ) -> CGFloat {
        placement.indicatorCenterX(in: tooltipWidth)
    }

    /// Picks the placement whose indicator lands closest to the centre of `anchor` once the tooltip
    /// has been kept inside the container. The indicator has only three possible positions, so near
    /// an edge the left or right variant reaches an anchor that the centred one cannot.
    static func indicatorPlacement(
        anchor: CGRect,
        containerSize: CGSize,
        tooltipWidth: CGFloat,
        pointsUp: Bool
    ) -> LemonadeTooltipIndicatorPlacement {
        let candidates: [LemonadeTooltipIndicatorPlacement] = pointsUp
            ? [.topCenter, .topLeft, .topRight]
            : [.bottomCenter, .bottomLeft, .bottomRight]

        let margin = LemonadeTooltipLayout.containerMargin
        let maxX = max(margin, containerSize.width - tooltipWidth - margin)

        var best = candidates[0]
        var bestError = CGFloat.greatestFiniteMagnitude

        for candidate in candidates {
            let offset = indicatorCenterOffset(placement: candidate, tooltipWidth: tooltipWidth)
            let x = min(max(anchor.midX - offset, margin), maxX)
            let error = abs(x + offset - anchor.midX)
            if error < bestError {
                bestError = error
                best = candidate
            }
        }

        return best
    }

    static func origin(
        anchor: CGRect,
        containerSize: CGSize,
        tooltipSize: CGSize,
        placement: LemonadeTooltipIndicatorPlacement,
        pointsUp: Bool
    ) -> CGPoint {
        let margin = LemonadeTooltipLayout.containerMargin
        let gap = LemonadeTooltipLayout.anchorGap
        let indicatorOffset = indicatorCenterOffset(
            placement: placement,
            tooltipWidth: tooltipSize.width
        )

        let maxX = max(margin, containerSize.width - tooltipSize.width - margin)
        let x = min(max(anchor.midX - indicatorOffset, margin), maxX)

        let maxY = max(margin, containerSize.height - tooltipSize.height - margin)
        let rawY = pointsUp
            ? anchor.maxY + gap
            : anchor.minY - gap - tooltipSize.height
        let y = min(max(rawY, margin), maxY)

        return CGPoint(x: x, y: y)
    }
}
