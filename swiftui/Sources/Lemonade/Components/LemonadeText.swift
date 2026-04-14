import SwiftUI

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

    /// Rich text component that parses inline tags and applies the corresponding style
    /// from the `tags` map. Untagged text inherits the base `textStyle` and `color`.
    ///
    /// Tags use the format `<name>content</name>`. This is localization-friendly because
    /// translators can reorder the tags freely within the translated string.
    ///
    /// ## Usage
    /// ```swift
    /// // The string can come from NSLocalizedString — tag order is language-dependent
    /// LemonadeUi.Text(
    ///     "It should arrive by <bold>15 June</bold>",
    ///     textStyle: LemonadeTypography.shared.bodyMediumRegular,
    ///     tags: ["bold": LemonadeTypography.shared.bodyMediumSemiBold]
    /// )
    /// ```
    @ViewBuilder
    static func Text(
        _ text: String,
        textStyle: LemonadeTextStyle = LemonadeTypography.shared.bodyMediumRegular,
        tags: [String: LemonadeTextStyle],
        textAlign: TextAlignment = .leading,
        color: Color = LemonadeTheme.colors.content.contentPrimary,
        tagColors: [String: Color] = [:],
        overflow: Text.TruncationMode = .tail,
        maxLines: Int? = nil,
        minLines: Int = 1
    ) -> some View {
        LemonadeTaggedTextView(
            text: text,
            baseTextStyle: textStyle,
            tags: tags,
            textAlign: textAlign,
            baseColor: color,
            tagColors: tagColors,
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

// MARK: - Tagged Text View

private struct LemonadeTaggedTextView: View {
    let text: String
    let baseTextStyle: LemonadeTextStyle
    let tags: [String: LemonadeTextStyle]
    let textAlign: TextAlignment
    let baseColor: Color
    let tagColors: [String: Color]
    let overflow: Text.TruncationMode
    let maxLines: Int?
    let minLines: Int

    var body: some View {
        let segments = Self.parseTaggedText(text)
        let combined = segments.reduce(SwiftUI.Text("")) { result, segment in
            let style = segment.tag.flatMap { tags[$0] } ?? baseTextStyle
            let color = segment.tag.flatMap { tagColors[$0] } ?? baseColor
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
        let tag: String?
    }

    private static func parseTaggedText(_ text: String) -> [Segment] {
        var segments: [Segment] = []
        let pattern = try! NSRegularExpression(pattern: "<(\\w+)>(.*?)</\\1>", options: [])
        let nsText = text as NSString
        var lastIndex = 0

        let matches = pattern.matches(in: text, range: NSRange(location: 0, length: nsText.length))
        for match in matches {
            let matchRange = match.range
            if matchRange.location > lastIndex {
                let plain = nsText.substring(with: NSRange(location: lastIndex, length: matchRange.location - lastIndex))
                segments.append(Segment(content: plain, tag: nil))
            }
            let tag = nsText.substring(with: match.range(at: 1))
            let content = nsText.substring(with: match.range(at: 2))
            segments.append(Segment(content: content, tag: tag))
            lastIndex = matchRange.location + matchRange.length
        }

        if lastIndex < nsText.length {
            let remaining = nsText.substring(from: lastIndex)
            segments.append(Segment(content: remaining, tag: nil))
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
