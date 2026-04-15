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
/// layout) below the description. For full per-row composable control, use the
/// standalone ``LemonadeUi/HistoryItem(label:subheading:description:voice:isCurrent:isLast:contentSlot:)``
/// inside your own `VStack` instead.
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
    /// ``LemonadeHistoryTimelineItem``. For full view-builder control per row, use the
    /// standalone ``LemonadeUi/HistoryItem(label:subheading:description:voice:isCurrent:isLast:contentSlot:)``
    /// inside your own `VStack` instead.
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

    /// A single row with a timeline indicator and a content stack of label,
    /// subheading, description, and an optional custom content slot.
    ///
    /// Use this standalone form when the caller needs full view-builder control per row;
    /// otherwise prefer ``LemonadeUi/HistoryTimeline(items:currentIndex:)`` which derives
    /// `isCurrent` and `isLast` automatically.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.HistoryItem(
    ///     label: "Paid",
    ///     subheading: "10:24",
    ///     description: "Payment was successfully processed.",
    ///     voice: .positive,
    ///     isCurrent: true
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - label: Primary row text.
    ///   - subheading: Optional secondary text shown immediately below the label.
    ///   - description: Optional tertiary paragraph shown below the subheading.
    ///   - voice: Semantic color of the indicator dot. Only applied when `isCurrent`
    ///     is `true`; non-current rows always render a muted dot.
    ///   - isCurrent: Whether this row is the current (active) step.
    ///   - isLast: Whether this is the last row in the timeline. Hides the connecting line.
    ///   - contentSlot: Optional view builder for custom content (e.g. a button or tag)
    ///     rendered below the description.
    @ViewBuilder
    static func HistoryItem<Content: View>(
        label: String,
        subheading: String? = nil,
        description: String? = nil,
        voice: LemonadeHistoryItemVoice = .neutral,
        isCurrent: Bool = false,
        isLast: Bool = false,
        @ViewBuilder contentSlot: @escaping () -> Content = { EmptyView() }
    ) -> some View {
        LemonadeHistoryItemView(
            label: label,
            subheading: subheading,
            description: description,
            voice: voice,
            isCurrent: isCurrent,
            isLast: isLast,
            contentSlot: contentSlot
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
                LemonadeHistoryItemView(
                    label: item.label,
                    subheading: item.subheading,
                    description: item.description,
                    voice: item.voice,
                    isCurrent: index == currentIndex,
                    isLast: index == items.count - 1,
                    contentSlot: {
                        if let slot = item.contentSlot {
                            slot()
                        } else {
                            EmptyView()
                        }
                    }
                )
            }
        }
    }
}

private struct LemonadeHistoryItemView<Content: View>: View {
    let label: String
    let subheading: String?
    let description: String?
    let voice: LemonadeHistoryItemVoice
    let isCurrent: Bool
    let isLast: Bool
    @ViewBuilder let contentSlot: () -> Content

    private var hasContentSlot: Bool {
        Content.self != EmptyView.self
    }

    private var dotColor: Color {
        isCurrent
            ? voice.currentColor
            : LemonadeTheme.colors.border.borderNeutralMedium
    }

    var body: some View {
        HStack(alignment: .top, spacing: LemonadeTheme.spaces.spacing300) {
            Color.clear
                .frame(width: HistoryTimelineMetrics.indicatorWidth)

            VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing200) {
                VStack(alignment: .leading, spacing: 0) {
                    LemonadeUi.Text(
                        label,
                        textStyle: LemonadeTypography.shared.bodyMediumMedium,
                        color: LemonadeTheme.colors.content.contentPrimary
                    )

                    if let subheading = subheading {
                        LemonadeUi.Text(
                            subheading,
                            textStyle: LemonadeTypography.shared.bodySmallRegular,
                            color: LemonadeTheme.colors.content.contentSecondary
                        )
                    }
                }

                if let description = description {
                    LemonadeUi.Text(
                        description,
                        textStyle: LemonadeTypography.shared.bodySmallRegular,
                        color: LemonadeTheme.colors.content.contentSecondary
                    )
                }

                if hasContentSlot {
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

                VStack(alignment: .leading, spacing: 0) {
                    LemonadeUi.HistoryItem(
                        label: "Positive current",
                        subheading: "Subheading",
                        voice: .positive,
                        isCurrent: true
                    )
                    LemonadeUi.HistoryItem(
                        label: "Critical current",
                        subheading: "Subheading",
                        voice: .critical,
                        isCurrent: true
                    )
                    LemonadeUi.HistoryItem(
                        label: "Neutral current",
                        subheading: "Subheading",
                        voice: .neutral,
                        isCurrent: true
                    )
                    LemonadeUi.HistoryItem(
                        label: "Neutral past",
                        subheading: "Subheading",
                        voice: .neutral,
                        isCurrent: false,
                        isLast: true
                    )
                }
            }
            .padding()
        }
        .previewLayout(.sizeThatFits)
    }
}
#endif
