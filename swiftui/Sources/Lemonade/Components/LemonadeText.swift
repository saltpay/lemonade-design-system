import SwiftUI

// MARK: - Text Override

/// Describes style overrides for a substring within rich text.
/// All properties are optional — only specified values override the base style.
public struct LemonadeTextOverride {
    /// The text style to apply to the matched substring.
    public let textStyle: LemonadeTextStyle?
    /// The color to apply to the matched substring.
    public let color: Color?

    public init(textStyle: LemonadeTextStyle? = nil, color: Color? = nil) {
        self.textStyle = textStyle
        self.color = color
    }
}

// MARK: - Text Component

public extension LemonadeUi {
    /// Text component for displaying styled text following the Lemonade Design System.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Text(
    ///     text: "Hello, World!",
    ///     textStyle: LemonadeTypography.shared.bodyMediumRegular
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - text: The string to display
    ///   - fontSize: Optional font size override. If not specified, uses the textStyle's fontSize.
    ///   - textStyle: The LemonadeTextStyle to apply. Defaults to bodyMediumRegular.
    ///   - textAlign: Text alignment. Defaults to leading.
    ///   - color: Text color. Defaults to contentPrimary.
    ///   - overflow: How to handle text overflow. Defaults to truncation.
    ///   - maxLines: Maximum number of lines. Defaults to unlimited.
    ///   - minLines: Minimum number of lines. Defaults to 1.
    /// - Returns: A styled Text view
    @ViewBuilder
    static func Text(
        _ text: String,
        fontSize: CGFloat? = nil,
        textStyle: LemonadeTextStyle = LemonadeTypography.shared.bodyMediumRegular,
        textAlign: TextAlignment = .leading,
        color: Color = LemonadeTheme.colors.content.contentPrimary,
        overflow: Text.TruncationMode = .tail,
        maxLines: Int? = nil,
        minLines: Int = 1
    ) -> some View {
        LemonadeTextView(
            text: text,
            fontSize: fontSize,
            textStyle: textStyle,
            textAlign: textAlign,
            color: color,
            overflow: overflow,
            maxLines: maxLines,
            minLines: minLines
        )
    }

    /// Text component with substring style overrides. Matches each key in `overrideStyle`
    /// within the text and applies the corresponding overrides to that substring.
    /// The rest of the text uses the base `textStyle` and `color`.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Text(
    ///     "Total: €129.99 (incl. tax)",
    ///     textStyle: LemonadeTypography.shared.bodyMediumRegular,
    ///     overrideStyle: [
    ///         "€129.99": LemonadeTextOverride(
    ///             textStyle: LemonadeTypography.shared.bodyLargeSemiBold,
    ///             color: LemonadeTheme.colors.content.contentPrimary
    ///         )
    ///     ]
    /// )
    /// ```
    @ViewBuilder
    static func Text(
        _ text: String,
        textStyle: LemonadeTextStyle = LemonadeTypography.shared.bodyMediumRegular,
        overrideStyle: [String: LemonadeTextOverride],
        textAlign: TextAlignment = .leading,
        color: Color = LemonadeTheme.colors.content.contentPrimary,
        overflow: Text.TruncationMode = .tail,
        maxLines: Int? = nil,
        minLines: Int = 1
    ) -> some View {
        LemonadeOverrideTextView(
            text: text,
            baseTextStyle: textStyle,
            overrideStyle: overrideStyle,
            textAlign: textAlign,
            baseColor: color,
            overflow: overflow,
            maxLines: maxLines,
            minLines: minLines
        )
    }

    /// Text component with a raw TextStyle for advanced customization.
    ///
    /// - Parameters:
    ///   - text: The string to display
    ///   - font: The Font to use
    ///   - overflow: How to handle text overflow. Defaults to truncation.
    ///   - maxLines: Maximum number of lines. Defaults to unlimited.
    ///   - minLines: Minimum number of lines. Defaults to 1.
    /// - Returns: A styled Text view
    @ViewBuilder
    static func Text(
        _ text: String,
        font: Font,
        overflow: Text.TruncationMode = .tail,
        maxLines: Int? = nil,
        minLines: Int = 1
    ) -> some View {
        LemonadeTextView(
            text: text,
            font: font,
            overflow: overflow,
            maxLines: maxLines,
            minLines: minLines
        )
    }
}

// MARK: - Internal Text View

private struct LemonadeTextView: View {
    let text: String
    let fontSize: CGFloat?
    let textStyle: LemonadeTextStyle?
    let font: Font?
    let textAlign: TextAlignment
    let color: Color
    let overflow: Text.TruncationMode
    let maxLines: Int?
    let minLines: Int

    init(
        text: String,
        fontSize: CGFloat? = nil,
        textStyle: LemonadeTextStyle = LemonadeTypography.shared.bodyMediumRegular,
        textAlign: TextAlignment = .leading,
        color: Color = LemonadeTheme.colors.content.contentPrimary,
        overflow: Text.TruncationMode = .tail,
        maxLines: Int? = nil,
        minLines: Int = 1
    ) {
        self.text = text
        self.fontSize = fontSize
        self.textStyle = textStyle
        self.font = nil
        self.textAlign = textAlign
        self.color = color
        self.overflow = overflow
        self.maxLines = maxLines
        self.minLines = minLines
    }

    init(
        text: String,
        font: Font,
        overflow: Text.TruncationMode = .tail,
        maxLines: Int? = nil,
        minLines: Int = 1
    ) {
        self.text = text
        self.fontSize = nil
        self.textStyle = nil
        self.font = font
        self.textAlign = .leading
        self.color = LemonadeTheme.colors.content.contentPrimary
        self.overflow = overflow
        self.maxLines = maxLines
        self.minLines = minLines
    }

    var body: some View {
        let displayText = isOverlineStyle ? text.uppercased() : text
        let resolvedFont = resolveFont()

        if #available(iOS 16.0, macOS 13.0, *) {
            SwiftUI.Text(displayText)
                .font(resolvedFont)
                .foregroundStyle(color)
                .multilineTextAlignment(textAlign)
                .lineLimit(maxLines)
                .truncationMode(overflow)
                .lineSpacing(textStyle?.lineSpacing ?? 0)
                .tracking(textStyle?.letterSpacing ?? 0)
                .frame(minHeight: textStyle?.lineHeight)
        } else {
            SwiftUI.Text(displayText)
                .font(resolvedFont)
                .foregroundStyle(color)
                .multilineTextAlignment(textAlign)
                .lineLimit(maxLines)
                .truncationMode(overflow)
                .lineSpacing(textStyle?.lineSpacing ?? 0)
                .frame(minHeight: textStyle?.lineHeight)
        }
    }


    private var isOverlineStyle: Bool {
        guard let style = textStyle else { return false }
        // Check if it's the overline style by checking for letter spacing
        return style.letterSpacing != nil && style.letterSpacing! > 0
    }

    private func resolveFont() -> Font {
        if let font = font {
            return font
        }

        guard let style = textStyle else {
            return .body
        }

        let size = fontSize ?? style.fontSize
        return .custom(LemonadeTypography.fontFamily, size: size).weight(style.fontWeight)
    }
}

// MARK: - Override Text View

private struct LemonadeOverrideTextView: View {
    let text: String
    let baseTextStyle: LemonadeTextStyle
    let overrideStyle: [String: LemonadeTextOverride]
    let textAlign: TextAlignment
    let baseColor: Color
    let overflow: Text.TruncationMode
    let maxLines: Int?
    let minLines: Int

    var body: some View {
        let segments = buildSegments()
        let combined = segments.reduce(SwiftUI.Text("")) { result, segment in
            let style = segment.override?.textStyle ?? baseTextStyle
            let color = segment.override?.color ?? baseColor
            let font: Font = .custom(LemonadeTypography.fontFamily, size: style.fontSize)
                .weight(style.fontWeight)
            var piece = SwiftUI.Text(segment.content)
                .font(font)
                .foregroundColor(color)
            if let tracking = style.letterSpacing {
                piece = piece.tracking(tracking)
            }
            return result + piece
        }

        combined
            .multilineTextAlignment(textAlign)
            .lineLimit(maxLines)
            .truncationMode(overflow)
            .lineSpacing(baseTextStyle.lineSpacing)
            .frame(minHeight: baseTextStyle.lineHeight)
    }

    private struct Segment {
        let content: String
        let override: LemonadeTextOverride?
    }

    private func buildSegments() -> [Segment] {
        var ranges: [(range: Range<String.Index>, key: String)] = []
        for key in overrideStyle.keys {
            var searchStart = text.startIndex
            while let foundRange = text.range(of: key, range: searchStart..<text.endIndex) {
                ranges.append((range: foundRange, key: key))
                searchStart = foundRange.upperBound
            }
        }
        ranges.sort { $0.range.lowerBound < $1.range.lowerBound }

        var segments: [Segment] = []
        var currentIndex = text.startIndex
        for entry in ranges {
            if entry.range.lowerBound > currentIndex {
                segments.append(Segment(content: String(text[currentIndex..<entry.range.lowerBound]), override: nil))
            }
            segments.append(Segment(content: String(text[entry.range]), override: overrideStyle[entry.key]))
            currentIndex = entry.range.upperBound
        }
        if currentIndex < text.endIndex {
            segments.append(Segment(content: String(text[currentIndex...]), override: nil))
        }
        return segments
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeText_Previews: PreviewProvider {
    static var previews: some View {
        VStack(alignment: .leading, spacing: 16) {
            LemonadeUi.Text(
                "Display Large",
                textStyle: LemonadeTypography.shared.displayLarge
            )

            LemonadeUi.Text(
                "Heading Medium",
                textStyle: LemonadeTypography.shared.headingMedium
            )

            LemonadeUi.Text(
                "Body Medium Regular",
                textStyle: LemonadeTypography.shared.bodyMediumRegular
            )

            LemonadeUi.Text(
                "Body Small SemiBold",
                textStyle: LemonadeTypography.shared.bodySmallSemiBold,
                color: LemonadeTheme.colors.content.contentSecondary
            )

            LemonadeUi.Text(
                "Overline Text",
                textStyle: LemonadeTypography.shared.bodyXSmallOverline
            )

            LemonadeUi.Text("This text allows multiple lines but is limited to 2 lines maximum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore.", textStyle: LemonadeTypography.shared.bodyMediumRegular, maxLines: 2)
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
