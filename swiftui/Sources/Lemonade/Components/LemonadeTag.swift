import SwiftUI

// MARK: - Tag Voice

/// Defines the tone of voice for a Tag component.
/// This affects the background color and text/icon tint.
public enum TagVoice {
    case neutral
    case critical
    case warning
    case info
    case positive

    var tintColor: Color {
        switch self {
        case .neutral: return LemonadeTheme.colors.content.contentPrimary
        case .critical: return LemonadeTheme.colors.content.contentCritical
        case .warning: return LemonadeTheme.colors.content.contentCaution
        case .info: return LemonadeTheme.colors.content.contentInfo
        case .positive: return LemonadeTheme.colors.content.contentPositive
        }
    }

    var containerColor: Color {
        switch self {
        case .neutral: return LemonadeTheme.colors.background.bgNeutralSubtle
        case .critical: return LemonadeTheme.colors.background.bgCriticalSubtle
        case .warning: return LemonadeTheme.colors.background.bgCautionSubtle
        case .info: return LemonadeTheme.colors.background.bgInfoSubtle
        case .positive: return LemonadeTheme.colors.background.bgPositiveSubtle
        }
    }
}

// MARK: - Tag Component

public extension LemonadeUi {
    /// A compact label used to categorize, organize, or annotate content.
    /// Typically static and non-interactive.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Tag(
    ///     label: "WARNING",
    ///     icon: .triangleAlert,
    ///     voice: .warning
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - label: String to be displayed in the tag
    ///   - icon: Optional LemonadeIcon to show as leading icon
    ///   - voice: TagVoice to define the tone. Defaults to .neutral
    /// - Returns: A styled Tag view
    @ViewBuilder
    static func Tag(
        label: String,
        icon: LemonadeIcon? = nil,
        voice: TagVoice = .neutral
    ) -> some View {
        LemonadeTagView(
            label: label,
            icon: icon,
            voice: voice
        )
    }
}

// MARK: - Internal Tag View

private struct LemonadeTagView: View {
    let label: String
    let icon: LemonadeIcon?
    let voice: TagVoice

    var body: some View {
        HStack(spacing: LemonadeTheme.spaces.spacing50) {
            if let icon = icon {
                LemonadeUi.Icon(
                    icon: icon,
                    contentDescription: nil,
                    size: .small,
                    tint: voice.tintColor
                )
            }

            LemonadeUi.Text(
                label,
                textStyle: LemonadeTypography.shared.bodyXSmallSemiBold,
                color: voice.tintColor,
                overflow: .tail,
                maxLines: 1
            )
            .padding(.horizontal, LemonadeTheme.spaces.spacing50)
        }
        .padding(.vertical, LemonadeTheme.spaces.spacing50)
        .padding(.horizontal, LemonadeTheme.spaces.spacing100)
        .background(voice.containerColor)
        .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius100))
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeTag_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 16) {
            // All voices without icon
            HStack(spacing: 8) {
                LemonadeUi.Tag(label: "Neutral", voice: .neutral)
                LemonadeUi.Tag(label: "Critical", voice: .critical)
                LemonadeUi.Tag(label: "Warning", voice: .warning)
            }

            HStack(spacing: 8) {
                LemonadeUi.Tag(label: "Info", voice: .info)
                LemonadeUi.Tag(label: "Positive", voice: .positive)
            }

            // All voices with icon
            VStack(alignment: .leading, spacing: 8) {
                LemonadeUi.Tag(label: "Neutral", icon: .heart, voice: .neutral)
                LemonadeUi.Tag(label: "Critical", icon: .circleX, voice: .critical)
                LemonadeUi.Tag(label: "Warning", icon: .triangleAlert, voice: .warning)
                LemonadeUi.Tag(label: "Info", icon: .circleInfo, voice: .info)
                LemonadeUi.Tag(label: "Positive", icon: .circleCheck, voice: .positive)
            }
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
