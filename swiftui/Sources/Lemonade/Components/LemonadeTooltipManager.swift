import SwiftUI

// MARK: - Tooltip Scrim

/// What the tooltip container draws behind a tooltip while it is showing.
///
/// Regardless of the mode, the container swallows taps outside the tooltip so an outside tap can
/// dismiss it — the UI underneath is not interactive while a tooltip is up.
public enum LemonadeTooltipScrim: Hashable, Sendable {
    /// Nothing is drawn behind the tooltip. The default for on-demand help.
    case none

    /// A translucent scrim covers the whole container.
    case dim

    /// As `dim`, but punched through around the anchor so the described element stays lit.
    case spotlight
}

// MARK: - Tooltip Step

/// One step of a guided tour. See ``LemonadeTooltipManager/startTour(steps:labels:scrim:showCloseButton:onFinish:onSkip:)``.
public struct LemonadeTooltipStep: Identifiable {
    public let id: UUID
    let anchor: String
    let content: String
    let title: String?
    let indicatorPlacement: LemonadeTooltipIndicatorPlacement?
    let cover: AnyView?

    /// - Parameters:
    ///   - anchor: Key of the element this step points at, as registered by
    ///     `View.lemonadeTooltipAnchor(_:)`. A step whose anchor is not currently on screen does not
    ///     render until it appears, so a tour can span several screens.
    ///   - content: The body text of the step.
    ///   - title: Optional bold heading.
    ///   - indicatorPlacement: Forces where the indicator sits. Defaults to `nil`, which resolves
    ///     the placement from where the anchor is on screen — always a top or bottom one, so pass a
    ///     left or right placement to put the tooltip beside its anchor instead.
    public init(
        id: UUID = UUID(),
        anchor: String,
        content: String,
        title: String? = nil,
        indicatorPlacement: LemonadeTooltipIndicatorPlacement? = nil
    ) {
        self.id = id
        self.anchor = anchor
        self.content = content
        self.title = title
        self.indicatorPlacement = indicatorPlacement
        self.cover = nil
    }

    /// Creates a step with a cover slot rendered above the text.
    public init<Cover: View>(
        id: UUID = UUID(),
        anchor: String,
        content: String,
        title: String? = nil,
        indicatorPlacement: LemonadeTooltipIndicatorPlacement? = nil,
        @ViewBuilder cover: () -> Cover
    ) {
        self.id = id
        self.anchor = anchor
        self.content = content
        self.title = title
        self.indicatorPlacement = indicatorPlacement
        self.cover = AnyView(cover())
    }
}

// MARK: - Tour Labels

/// Text the container puts in a tour's footer. Pass translated strings to localise a tour.
public struct LemonadeTooltipTourLabels: Sendable {
    let next: String
    let done: String
    let skip: String?
    let close: String
    let stepSeparator: String

    /// - Parameters:
    ///   - next: Label of the action that advances to the next step.
    ///   - done: Label that replaces `next` on the final step.
    ///   - skip: Label of the action that abandons the tour. Pass `nil` to leave it out.
    ///   - close: Accessibility label for the close button. Kept separate from `skip`, which can be
    ///     `nil` — the close button is always there, so it always needs a label.
    ///   - stepSeparator: Word between the two numbers of the step counter, as in `1 of 3`.
    public init(
        next: String = "Next",
        done: String = "Done",
        skip: String? = "Skip",
        close: String = "Close",
        stepSeparator: String = "of"
    ) {
        self.next = next
        self.done = done
        self.skip = skip
        self.close = close
        self.stepSeparator = stepSeparator
    }
}

// MARK: - Tooltip Presentation

struct LemonadeTooltipPresentation: Identifiable {
    let id: UUID
    let anchor: String
    let content: String
    let title: String?
    let indicatorPlacement: LemonadeTooltipIndicatorPlacement?
    let scrim: LemonadeTooltipScrim
    let dismissOnOutsideTap: Bool
    let showCloseButton: Bool
    let closeContentDescription: String?
    let cover: AnyView?
    let footer: ((LemonadeTooltipFooterScope) -> AnyView)?
    let isTourStep: Bool
    let stepIndex: Int
    let stepCount: Int
    /// Captured rather than read back off the manager: the tour is cleared the moment it is
    /// dismissed, so a presentation retained for the exit animation has to carry its own labels or
    /// the footer vanishes a frame into the fade.
    let tourLabels: LemonadeTooltipTourLabels?
}

private struct LemonadeTooltipTour {
    let steps: [LemonadeTooltipStep]
    let labels: LemonadeTooltipTourLabels
    let scrim: LemonadeTooltipScrim
    let showCloseButton: Bool
    let dismissOnOutsideTap: Bool
    let onFinish: () -> Void
    let onSkip: () -> Void
}

// MARK: - Tooltip Manager

/// A manager for showing tooltips anchored to elements on screen.
///
/// A tooltip always points at an element, so the element has to be tagged with
/// `View.lemonadeTooltipAnchor(_:)` before anything can be shown against it. Only one tooltip is
/// visible at a time and showing another replaces it. Nothing is queued: unlike a toast, a tooltip
/// describes something on screen, so a queued one would usually be stale by the time it surfaced.
///
/// ## Setup
/// Add the tooltip container modifier to your root view:
/// ```swift
/// ContentView()
///     .lemonadeTooltipContainer()
/// ```
///
/// ## Usage
/// ```swift
/// struct MyView: View {
///     @EnvironmentObject private var tooltips: LemonadeTooltipManager
///
///     var body: some View {
///         LemonadeUi.IconButton(
///             icon: .circleInfo,
///             contentDescription: "About fees",
///             onClick: {
///                 tooltips.show(
///                     anchor: "fees-info",
///                     content: "Fees are deducted daily."
///                 )
///             }
///         )
///         .lemonadeTooltipAnchor("fees-info")
///     }
/// }
/// ```
@MainActor
public final class LemonadeTooltipManager: ObservableObject {

    @Published private(set) var presentation: LemonadeTooltipPresentation?

    /// Anchor frames in global coordinates, keyed by anchor key.
    @Published private(set) var anchors: [String: CGRect] = [:]

    /// The container's own frame in global coordinates, used to translate anchors into it.
    @Published var containerFrame: CGRect = .zero

    private var tour: LemonadeTooltipTour?

    private var stepIndex: Int = 0

    public init() {}

    /// Index of the step being shown, or `-1` when no tour is running.
    public var currentStepIndex: Int {
        tour == nil ? -1 : stepIndex
    }

    /// Number of steps in the running tour, or `0` when none is running.
    public var tourStepCount: Int {
        tour?.steps.count ?? 0
    }

    /// Whether a tooltip is currently being presented.
    public var isVisible: Bool {
        presentation != nil
    }

    /// Shows a single tooltip against `anchor`. Anything already showing is replaced.
    ///
    /// - Parameters:
    ///   - anchor: Key of the element to point at, as registered by `View.lemonadeTooltipAnchor(_:)`.
    ///     Nothing renders until an element with this key is on screen.
    ///   - content: The body text.
    ///   - title: Optional bold heading.
    ///   - indicatorPlacement: Forces where the indicator sits. Defaults to `nil`, which resolves
    ///     the placement from where the anchor is on screen — always a top or bottom one, so pass a
    ///     left or right placement to put the tooltip beside its anchor instead.
    ///   - scrim: What to draw behind the tooltip. Defaults to `.none` — on-demand help usually
    ///     should not dim the screen.
    ///   - dismissOnOutsideTap: Whether a tap outside the tooltip dismisses it. Defaults to `true`.
    ///     Either way the container swallows the tap, so the UI underneath is never acted on by
    ///     accident.
    ///   - showCloseButton: Whether to show the close button. Defaults to `false`.
    ///   - closeContentDescription: Accessibility label for the close button.
    public func show(
        anchor: String,
        content: String,
        title: String? = nil,
        indicatorPlacement: LemonadeTooltipIndicatorPlacement? = nil,
        scrim: LemonadeTooltipScrim = .none,
        dismissOnOutsideTap: Bool = true,
        showCloseButton: Bool = false,
        closeContentDescription: String? = nil
    ) {
        present(
            anchor: anchor,
            content: content,
            title: title,
            indicatorPlacement: indicatorPlacement,
            scrim: scrim,
            dismissOnOutsideTap: dismissOnOutsideTap,
            showCloseButton: showCloseButton,
            closeContentDescription: closeContentDescription,
            cover: nil,
            footer: nil
        )
    }

    /// Shows a single tooltip with a footer. See ``show(anchor:content:title:indicatorPlacement:scrim:dismissOnOutsideTap:showCloseButton:closeContentDescription:)``.
    ///
    /// - Parameter footer: Footer slot. See ``LemonadeTooltipFooterScope`` for the parts that
    ///   belong in it.
    public func show<Footer: View>(
        anchor: String,
        content: String,
        title: String? = nil,
        indicatorPlacement: LemonadeTooltipIndicatorPlacement? = nil,
        scrim: LemonadeTooltipScrim = .none,
        dismissOnOutsideTap: Bool = true,
        showCloseButton: Bool = false,
        closeContentDescription: String? = nil,
        @ViewBuilder footer: @escaping (LemonadeTooltipFooterScope) -> Footer
    ) {
        present(
            anchor: anchor,
            content: content,
            title: title,
            indicatorPlacement: indicatorPlacement,
            scrim: scrim,
            dismissOnOutsideTap: dismissOnOutsideTap,
            showCloseButton: showCloseButton,
            closeContentDescription: closeContentDescription,
            cover: nil,
            footer: { scope in AnyView(footer(scope)) }
        )
    }

    /// Starts a guided tour. The container builds each step's footer itself — the step counter, the
    /// skip action and the next/done action — so a caller only describes the steps.
    ///
    /// ## Usage
    /// ```swift
    /// tooltips.startTour(
    ///     steps: [
    ///         LemonadeTooltipStep(
    ///             anchor: "takings",
    ///             content: "Everything you sold today.",
    ///             title: "Daily takings"
    ///         )
    ///     ],
    ///     onFinish: { markOnboardingSeen() }
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - steps: The steps, in order. An empty list does nothing.
    ///   - labels: Text for the generated footer. Pass translated strings to localise a tour.
    ///   - scrim: What to draw behind each step. Defaults to `.spotlight`, which keeps the element
    ///     being described lit while dimming the rest.
    ///   - showCloseButton: Whether each step shows the close button. Defaults to `true`. Pass
    ///     `false` for a tour whose only exits are the next/done action and an outside tap.
    ///   - dismissOnOutsideTap: Whether a tap outside the tooltip abandons the tour. Defaults to
    ///     `true`. Pass `false` to require the next/done action — the tap is still swallowed, so the
    ///     UI underneath is never acted on, it just does not end the tour.
    ///   - onFinish: Invoked once the final step is confirmed.
    ///   - onSkip: Invoked when the tour is abandoned, whether by the skip action, the close button
    ///     or an outside tap.
    public func startTour(
        steps: [LemonadeTooltipStep],
        labels: LemonadeTooltipTourLabels = LemonadeTooltipTourLabels(),
        scrim: LemonadeTooltipScrim = .spotlight,
        showCloseButton: Bool = true,
        dismissOnOutsideTap: Bool = true,
        onFinish: @escaping () -> Void = {},
        onSkip: @escaping () -> Void = {}
    ) {
        guard !steps.isEmpty else { return }

        tour = LemonadeTooltipTour(
            steps: steps,
            labels: labels,
            scrim: scrim,
            showCloseButton: showCloseButton,
            dismissOnOutsideTap: dismissOnOutsideTap,
            onFinish: onFinish,
            onSkip: onSkip
        )
        stepIndex = 0
        presentCurrentStep()
    }

    /// Advances to the next step, finishing the tour if the current step is the last one.
    public func next() {
        guard let tour else { return }

        if stepIndex >= tour.steps.count - 1 {
            self.tour = nil
            presentation = nil
            tour.onFinish()
            return
        }

        stepIndex += 1
        presentCurrentStep()
    }

    /// Goes back one step. Does nothing on the first step.
    public func previous() {
        guard tour != nil, stepIndex > 0 else { return }

        stepIndex -= 1
        presentCurrentStep()
    }

    /// Abandons the running tour and invokes its `onSkip`.
    public func skip() {
        guard let tour else { return }

        self.tour = nil
        presentation = nil
        tour.onSkip()
    }

    /// Dismisses whatever is showing. A running tour is abandoned, which invokes its `onSkip`.
    public func dismiss() {
        if tour != nil {
            skip()
            return
        }

        presentation = nil
    }

    func updateAnchor(_ key: String, frame: CGRect) {
        guard anchors[key] != frame else { return }
        anchors[key] = frame
    }

    func removeAnchor(_ key: String) {
        anchors.removeValue(forKey: key)
    }

    /// Anchor frame translated into the container's own coordinate space, or `nil` if not on screen.
    func anchorFrameInContainer(_ key: String) -> CGRect? {
        guard let frame = anchors[key] else { return nil }
        return frame.offsetBy(dx: -containerFrame.minX, dy: -containerFrame.minY)
    }

    private func presentCurrentStep() {
        guard let tour else { return }
        let step = tour.steps[stepIndex]

        presentation = LemonadeTooltipPresentation(
            id: UUID(),
            anchor: step.anchor,
            content: step.content,
            title: step.title,
            indicatorPlacement: step.indicatorPlacement,
            scrim: tour.scrim,
            dismissOnOutsideTap: tour.dismissOnOutsideTap,
            showCloseButton: tour.showCloseButton,
            closeContentDescription: tour.labels.close,
            cover: step.cover,
            footer: nil,
            isTourStep: true,
            stepIndex: stepIndex,
            stepCount: tour.steps.count,
            tourLabels: tour.labels
        )
    }

    private func present(
        anchor: String,
        content: String,
        title: String?,
        indicatorPlacement: LemonadeTooltipIndicatorPlacement?,
        scrim: LemonadeTooltipScrim,
        dismissOnOutsideTap: Bool,
        showCloseButton: Bool,
        closeContentDescription: String?,
        cover: AnyView?,
        footer: ((LemonadeTooltipFooterScope) -> AnyView)?
    ) {
        tour = nil
        presentation = LemonadeTooltipPresentation(
            id: UUID(),
            anchor: anchor,
            content: content,
            title: title,
            indicatorPlacement: indicatorPlacement,
            scrim: scrim,
            dismissOnOutsideTap: dismissOnOutsideTap,
            showCloseButton: showCloseButton,
            closeContentDescription: closeContentDescription,
            cover: cover,
            footer: footer,
            isTourStep: false,
            stepIndex: 0,
            stepCount: 0,
            tourLabels: nil
        )
    }
}
