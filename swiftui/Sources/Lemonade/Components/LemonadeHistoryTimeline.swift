import SwiftUI

// MARK: - LemonadeHistoryItemVoice

/// Defines the semantic voice of a HistoryItem indicator dot when rendered as the current step.
public enum LemonadeHistoryItemVoice {
    case neutral
    case positive
    case critical

    var currentColor: Color {
        switch self {
        case .neutral: return LemonadeTheme.colors.background.bgNeutral
        case .positive: return LemonadeTheme.colors.background.bgPositive
        case .critical: return LemonadeTheme.colors.background.bgCritical
        }
    }
}

// MARK: - LemonadeHistoryTimelineItem

/// Describes a single row inside a ``LemonadeUi/HistoryTimeline(items:currentIndex:)``.
///
/// Use `contentSlot` to inject arbitrary view content (e.g. a button, tag, or nested
/// layout) below the description.
public struct LemonadeHistoryTimelineItem {
    public let label: String
    public let subheading: String?
    public let description: String?
    public let voice: LemonadeHistoryItemVoice
    let contentSlot: (() -> AnyView)?

    /// Creates a history timeline row description.
    ///
    /// - Parameters:
    ///   - label: Primary row text.
    ///   - subheading: Optional secondary text shown immediately below the label.
    ///   - description: Optional tertiary paragraph shown below the subheading.
    ///   - voice: Semantic color of the indicator dot when this row is the current step.
    ///   - contentSlot: Optional view builder for custom content rendered below the
    ///     description (e.g. a button or tag).
    public init<Content: View>(
        label: String,
        subheading: String? = nil,
        description: String? = nil,
        voice: LemonadeHistoryItemVoice = .neutral,
        @ViewBuilder contentSlot: @escaping () -> Content
    ) {
        self.label = label
        self.subheading = subheading
        self.description = description
        self.voice = voice
        self.contentSlot = { AnyView(contentSlot()) }
    }

    /// Creates a history timeline row description without a custom content slot.
    public init(
        label: String,
        subheading: String? = nil,
        description: String? = nil,
        voice: LemonadeHistoryItemVoice = .neutral
    ) {
        self.label = label
        self.subheading = subheading
        self.description = description
        self.voice = voice
        self.contentSlot = nil
    }
}

// MARK: - Public API

public extension LemonadeUi {
    /// A vertical list of history steps with a left-aligned indicator column.
    ///
    /// Each row shows a colored dot; the current row uses the item's
    /// ``LemonadeHistoryItemVoice`` color, while all other rows use a muted neutral.
    /// A connecting line joins every row except the last.
    ///
    /// For rows with custom content (buttons, tags, etc.), set `contentSlot` on the
    /// ``LemonadeHistoryTimelineItem``.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.HistoryTimeline(
    ///     items: [
    ///         LemonadeHistoryTimelineItem(
    ///             label: "Find a Visa PayPoint next to you",
    ///             description: "PayPoint locations collect cash deposits on our behalf",
    ///             voice: .positive
    ///         ) {
    ///             LemonadeUi.Button(label: "Find a PayPoint") { /* ... */ }
    ///         },
    ///         LemonadeHistoryTimelineItem(
    ///             label: "Enter the amount and generate a barcode",
    ///             description: "Show the barcode to the shopkeeper and deposit the funds"
    ///         )
    ///     ],
    ///     currentIndex: 0
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - items: Ordered list of rows to display.
    ///   - currentIndex: Index of the row to render as the current step. Pass `nil`
    ///     to render every row in its non-current (muted) state. Defaults to `0`.
    @ViewBuilder
    static func HistoryTimeline(
        items: [LemonadeHistoryTimelineItem],
        currentIndex: Int? = 0
    ) -> some View {
        LemonadeHistoryTimelineView(
            items: items,
            currentIndex: currentIndex
        )
    }
}

// MARK: - Internal Views

private enum HistoryTimelineMetrics {
    static let indicatorWidth: CGFloat = 20
    static let dotSize: CGFloat = 10
    static let dotTopOffset: CGFloat = 7
    static let lineThickness: CGFloat = 1
}

private struct LemonadeHistoryTimelineView: View {
    let items: [LemonadeHistoryTimelineItem]
    let currentIndex: Int?

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            ForEach(Array(items.enumerated()), id: \.offset) { index, item in
                CoreHistoryTimelineItemView(
                    item: item,
                    isCurrent: index == currentIndex,
                    isLast: index == items.count - 1
                )
            }
        }
    }
}

private struct CoreHistoryTimelineItemView: View {
    let item: LemonadeHistoryTimelineItem
    let isCurrent: Bool
    let isLast: Bool

    private var dotColor: Color {
        isCurrent
            ? item.voice.currentColor
            : LemonadeTheme.colors.border.borderNeutralMedium
    }

    var body: some View {
        HStack(alignment: .top, spacing: LemonadeTheme.spaces.spacing300) {
            Color.clear
                .frame(width: HistoryTimelineMetrics.indicatorWidth)

            VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing200) {
                VStack(alignment: .leading, spacing: 0) {
                    LemonadeUi.Text(
                        item.label,
                        textStyle: LemonadeTypography.shared.bodyMediumMedium,
                        color: LemonadeTheme.colors.content.contentPrimary
                    )

                    if let subheading = item.subheading {
                        LemonadeUi.Text(
                            subheading,
                            textStyle: LemonadeTypography.shared.bodySmallRegular,
                            color: LemonadeTheme.colors.content.contentSecondary
                        )
                    }
                }

                if let description = item.description {
                    LemonadeUi.Text(
                        description,
                        textStyle: LemonadeTypography.shared.bodySmallRegular,
                        color: LemonadeTheme.colors.content.contentSecondary
                    )
                }

                if let contentSlot = item.contentSlot {
                    contentSlot()
                }
            }
            .padding(.bottom, isLast ? 0 : LemonadeTheme.spaces.spacing400)
            .frame(maxWidth: .infinity, alignment: .leading)
        }
        .background(alignment: .topLeading) {
            HistoryItemIndicator(dotColor: dotColor, isLast: isLast)
        }
    }
}

private struct HistoryItemIndicator: View {
    let dotColor: Color
    let isLast: Bool

    var body: some View {
        VStack(spacing: 0) {
            Spacer()
                .frame(height: HistoryTimelineMetrics.dotTopOffset)

            Circle()
                .fill(dotColor)
                .frame(
                    width: HistoryTimelineMetrics.dotSize,
                    height: HistoryTimelineMetrics.dotSize
                )

            if !isLast {
                Rectangle()
                    .fill(LemonadeTheme.colors.border.borderNeutralMedium)
                    .frame(width: HistoryTimelineMetrics.lineThickness)
                    .frame(maxHeight: .infinity)
            }
        }
        .frame(width: HistoryTimelineMetrics.indicatorWidth)
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeHistoryTimeline_Previews: PreviewProvider {
    static var previews: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing600) {
                LemonadeUi.HistoryTimeline(
                    items: [
                        LemonadeHistoryTimelineItem(
                            label: "Find a Visa PayPoint next to you",
                            description: "PayPoint locations collect cash deposits on our behalf",
                            voice: .positive
                        ),
                        LemonadeHistoryTimelineItem(
                            label: "Enter the amount and generate a barcode",
                            description: "Show the barcode to the shopkeeper and deposit the funds"
                        ),
                        LemonadeHistoryTimelineItem(
                            label: "After deposit, your money will be available in 10 minutes",
                            description: "We'll notify you once the funds are in your account"
                        )
                    ]
                )

                LemonadeUi.HistoryTimeline(
                    items: [
                        LemonadeHistoryTimelineItem(
                            label: "Find a Visa PayPoint next to you",
                            description: "PayPoint locations collect cash deposits on our behalf",
                            voice: .positive
                        ) {
                            LemonadeUi.Button(
                                label: "Find a PayPoint",
                                onClick: {},
                                variant: .secondary
                            )
                        },
                        LemonadeHistoryTimelineItem(
                            label: "Enter the amount and generate a barcode",
                            description: "Show the barcode to the shopkeeper and deposit the funds"
                        ),
                        LemonadeHistoryTimelineItem(
                            label: "After deposit, your money will be available in 10 minutes",
                            description: "We'll notify you once the funds are in your account"
                        )
                    ],
                    currentIndex: 0
                )

                LemonadeUi.HistoryTimeline(
                    items: [
                        LemonadeHistoryTimelineItem(
                            label: "Positive current",
                            subheading: "Subheading",
                            voice: .positive
                        )
                    ]
                )

                LemonadeUi.HistoryTimeline(
                    items: [
                        LemonadeHistoryTimelineItem(
                            label: "Critical current",
                            subheading: "Subheading",
                            voice: .critical
                        )
                    ]
                )

                LemonadeUi.HistoryTimeline(
                    items: [
                        LemonadeHistoryTimelineItem(
                            label: "Neutral current",
                            subheading: "Subheading",
                            voice: .neutral
                        )
                    ]
                )

                LemonadeUi.HistoryTimeline(
                    items: [
                        LemonadeHistoryTimelineItem(
                            label: "Neutral past",
                            subheading: "Subheading",
                            voice: .neutral
                        )
                    ],
                    currentIndex: nil
                )
            }
            .padding()
        }
        .previewLayout(.sizeThatFits)
    }
}
#endif
